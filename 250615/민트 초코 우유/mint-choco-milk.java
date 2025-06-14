import java.io.*;
import java.util.*;

class Leader implements Comparable<Leader>{
	int x;
	int y;
	int amount;
	
	public Leader(int x, int y, int amount) {
		super();
		this.x = x;
		this.y = y;
		this.amount = amount;
	}

	@Override
	public int compareTo(Leader o) {
		if (o.amount != this.amount) {
			return Integer.compare(o.amount, this.amount);
		} else if (o.x != this.x) {
			return Integer.compare(this.x, o.x);
		} else {
			return Integer.compare(this.y, o.y);
		}
	}
}

public class Main {
	
	static int N;
	static int[] dx = {-1, 1, 0, 0};
	static int[] dy = {0, 0, -1, 1};
	static boolean[][] visited;
	static String[][] food;
	static int[][] amount;
	static List<int[]> leaders;
	static HashSet<String> defense;
	
	// 범위 내인지
	static boolean isIn(int x, int y) {
		if (x >= 0 && x < N && y >= 0 && y < N) {
			return true;
		}
		return false;
	}
	
	// 점심
	static void lunch(int x, int y) {
		
		// bfs에 씀
		Queue<int[]> queue = new ArrayDeque<>();
		queue.add(new int[] {x, y});

		visited[x][y] = true;
		
		// 그룹원들 모음 (행, 열, 신앙심)
		List<int[]> list = new ArrayList<>();
		list.add(new int[] {x, y, amount[x][y]});
		
		// 그룹 찾을 음식
		String targetFood = food[x][y];
		
		// 그룹 찾기
		while (!queue.isEmpty()) {
			int[] temp = queue.poll();
			
			for (int d = 0; d < 4; d++) {
				int nx = temp[0] + dx[d];
				int ny = temp[1] + dy[d];
				
				if (isIn(nx, ny) && !visited[nx][ny] && food[nx][ny].equals(targetFood)) {
					queue.add(new int[] {nx, ny});
					list.add(new int[] {nx, ny, amount[nx][ny]});
					
					visited[nx][ny] = true;
				}
			}
		}
		
		// 대표 찾기
		int[] tempLeader = {-1, 0, 0};
		
		for (int[] item: list) {
			if (tempLeader[2] < item[2]) {
				tempLeader[0] = item[0];
				tempLeader[1] = item[1];
				tempLeader[2] = item[2];
			} else if (tempLeader[2] == item[2]) {
				if (tempLeader[0] > item[0]) {
					tempLeader[0] = item[0];
					tempLeader[1] = item[1];
					tempLeader[2] = item[2];
				} else if (tempLeader[0] == item[0]) {
					if (tempLeader[1] > item[1]) {
						tempLeader[0] = item[0];
						tempLeader[1] = item[1];
						tempLeader[2] = item[2];
					}
				}
			}
		}
				
		// 신앙심 넘기기
		for (int[] item: list) {
			if (item[0] == tempLeader[0] && item[1] == tempLeader[1]) {
				tempLeader[2] += list.size() - 1;
				amount[item[0]][item[1]] += list.size() - 1;
			} else {
				amount[item[0]][item[1]]--;
			}
		}
				
		// 리더 저장
		leaders.add(new int[] {tempLeader[0], tempLeader[1], amount[tempLeader[0]][tempLeader[1]]});
		
	}
	
	// 저녁 전파
	static void spread(Leader oneLeader, int ganjeol, int way) {
		
		int x = oneLeader.x;
		int y = oneLeader.y;
		int nx = x;
		int ny = y;
		
		while (true) {
			nx += dx[way];
			ny += dy[way];
			
			// 격자 밖으로 나감
			if (!isIn(nx, ny)) {
				return;
			}
			
			// 간절함이 0
			if (ganjeol == 0) {
				return;
			}
			
			if (!food[x][y].equals(food[nx][ny])) {
				// 강한 전파
				if (ganjeol > amount[nx][ny]) {

					food[nx][ny] = food[x][y];
					ganjeol -= (amount[nx][ny] + 1);
					if (ganjeol <= 0) {
						return;
					}
					amount[nx][ny]++;
					
					defense.add(nx + "," + ny);
					
				// 약한 전파
				} else {
					HashSet<Character> set = new HashSet<Character>();
					
					for (int i = 0; i < food[x][y].length(); i++) {
						set.add(food[x][y].charAt(i));
					}
					
					for (int i = 0; i < food[nx][ny].length(); i++) {
						set.add(food[nx][ny].charAt(i));
					}
					
					List<Character> list = new ArrayList<Character>(set);
					Collections.sort(list);
					
					String newFood = "";
					for (Character cha: list) {
						newFood += cha;
					}
					
					food[nx][ny] = newFood;
					defense.add(nx + "," + ny);
					
					amount[nx][ny] += ganjeol;
					ganjeol = 0;
					return;
				}
				
			}
		}
	}
	
	// 방어모드인지 확인
	static boolean isDefense(Leader leader) {
		for (String item: defense) {
			if (item.equals(leader.x + "," + leader.y)) {
				return true;
			}
		}
		
		return false;
	}
	
	public static void main(String[] args) throws Exception {
		
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		StringTokenizer st = new StringTokenizer(br.readLine());
		
		// 책상 배열 크기
		N = Integer.parseInt(st.nextToken());
		// T일 동안 진행
		int T = Integer.parseInt(st.nextToken());
		
		// 신봉 음식
		food = new String[N][N];
		
		for (int i = 0; i < N; i++) {
			String f = br.readLine();
			for (int j = 0; j < N; j++) {
				food[i][j] = f.charAt(j) + "";
			}
		}
		
		// 신앙심
		amount = new int[N][N];
		
		for (int i = 0; i < N; i++) {
			st = new StringTokenizer(br.readLine());
			for (int j = 0; j < N; j++) {
				amount[i][j] = Integer.parseInt(st.nextToken());
			}
		}
		
		// T일 반복
		for (int t = 0; t < T; t++) {
			
			// 1. 아침 시간: 신앙심 1씩 증가
			for (int i = 0; i < N; i++) {
				for (int j = 0; j < N; j++) {
					amount[i][j]++;
				}
			}
			
			// 2. 점심 시간: 그룹 형성
			visited = new boolean[N][N];
			
			// 대표자들
			leaders = new ArrayList<>();
			
			for (int i = 0; i < N; i++) {
				for (int j = 0; j < N; j++) {
					if (!visited[i][j]) {
						lunch(i, j);
					}
				}
			}

			// 3. 저녁 시간
			// 방어상태
			defense = new HashSet<String>();
			
			// 대표자 우선순위 큐에 넣기
			PriorityQueue<Leader> oneFood = new PriorityQueue<Leader>();
			PriorityQueue<Leader> twoFood = new PriorityQueue<Leader>();
			PriorityQueue<Leader> threeFood = new PriorityQueue<Leader>();
			
			for (int[] l: leaders) {
				int x = l[0];
				int y = l[1];
				int a = l[2];
				if (food[x][y].length() == 1) {
					oneFood.add(new Leader(x, y, a));
				} else if (food[x][y].length() == 2) {
					twoFood.add(new Leader(x, y, a));
				} else {
					threeFood.add(new Leader(x, y, a));
				}
			}
			
			// 단일 음식 
			while (!oneFood.isEmpty()) {
				Leader oneLeader = oneFood.poll();
				
				if (!isDefense(oneLeader)) {
					// 최초 신앙심
					int B = amount[oneLeader.x][oneLeader.y];
					
					// 간절함
					int ganjeol = amount[oneLeader.x][oneLeader.y] - 1;
					amount[oneLeader.x][oneLeader.y] = 1;
					
					// 전파 방향
					int way = B % 4;
					
					spread(oneLeader, ganjeol, way);
				}
			}
			
			// 이중 조합
			while (!twoFood.isEmpty()) {
				Leader twoLeader = twoFood.poll();
				
				if (!isDefense(twoLeader)) {
					// 최초 신앙심
					int B = amount[twoLeader.x][twoLeader.y];

					// 간절함
					int ganjeol = amount[twoLeader.x][twoLeader.y] - 1;
					amount[twoLeader.x][twoLeader.y] = 1;
					
					// 전파 방향
					int way = B % 4;
					
					spread(twoLeader, ganjeol, way);
				}
			}
			
			// 삼중 조합
			while (!threeFood.isEmpty()) {
				Leader threeLeader = threeFood.poll();
				
				if (!isDefense(threeLeader)) {
					// 최초 신앙심
					int B = amount[threeLeader.x][threeLeader.y];

					// 간절함
					int ganjeol = amount[threeLeader.x][threeLeader.y] - 1;
					amount[threeLeader.x][threeLeader.y] = 1;
					
					// 전파 방향
					int way = B % 4;
					
					spread(threeLeader, ganjeol, way);
				}
			}

			int CMT = 0;
			int CT = 0;
			int MT = 0;
			int CM = 0;
			int M = 0;
			int C = 0;
			int MINT = 0;
			
			for (int i = 0; i < N; i++) {
				for (int j = 0; j < N; j++) {
					if (food[i][j].equals("CMT")) {
						CMT += amount[i][j];
					}
					
					if (food[i][j].equals("CT")) {
						CT += amount[i][j];
					}
					
					if (food[i][j].equals("MT")) {
						MT += amount[i][j];
					}
					
					if (food[i][j].equals("CM")) {
						CM += amount[i][j];
					}
					
					if (food[i][j].equals("M")) {
						M += amount[i][j];
					}
					
					if (food[i][j].equals("C")) {
						C += amount[i][j];
					}
					
					if (food[i][j].equals("T")) {
						MINT += amount[i][j];
					}
				}
			}			
			
			StringBuilder sb = new StringBuilder();
			sb.append(CMT).append(" ").append(CT).append(" ").append(MT).append(" ").append(CM).append(" ").append(M).append(" ").append(C).append(" ").append(MINT);
			
			System.out.println(sb);
		}
	}
}
