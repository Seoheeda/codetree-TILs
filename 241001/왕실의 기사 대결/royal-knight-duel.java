import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Queue;
import java.util.Set;
import java.util.StringTokenizer;

public class Main {
	
	static int L;
	static int[] dx = {-1, 0, 1, 0};
	static int[] dy = {0, 1, 0, -1};
	static int[][] arr, knight, rc;
	static int[] strength, damages;
	static List<int[]> list;
	
	// arr 범위 안에 있나 확인
	static boolean ok(int x, int y) {
		if (x >= 0 && x < L && y >= 0 && y < L) {
			return true;
		}
		
		return false;
	}
	
	// n번 기사 체력 남았는지 확인
	static boolean alive(int n) {
		if (strength[n] > 0) {
			return true;
		}
		
		return false;
	}
	
	// 명령 수행하기
	static void order(int n, int d) {

		// 이동 불가능하면 리턴
		if (!can(n, d)) {
			return;
		}

		Set<Integer> set = new HashSet<Integer>();
		
		for (int i = 0; i < list.size(); i++) {
			int x = list.get(i)[0];
			int y = list.get(i)[1];
			int num = list.get(i)[2];
			
			knight[x][y] = 0;
			
			set.add(num);
		}
		
		 for (int num : set) {
		        rc[num][0] += dx[d];
		        rc[num][1] += dy[d];
		 }
		
		for (int i = 0; i < list.size(); i++) {
			int nx = list.get(i)[0] + dx[d];
			int ny = list.get(i)[1] + dy[d];
			int num = list.get(i)[2];
			
			if (strength[num] > 0) {
				knight[nx][ny] = num;
				
				if (arr[nx][ny] == 1 && num != n) {
					strength[num]--;
					damages[num]++;
				}
			}		
		}				
	}
	
	// 이동 가능한지 보기
	static boolean can(int n, int d) {
		list = new ArrayList<int[]>();
		boolean[][] visited = new boolean[L][L];
		
		Queue<int[]> queue = new ArrayDeque<int[]>();
		int[] temp = new int[3];
		temp[0] = rc[n][0];
		temp[1] = rc[n][1];
		temp[2] = n;
		queue.add(temp);
		visited[temp[0]][temp[1]] = true;
		list.add(new int[] {temp[0], temp[1], temp[2]});
		
		while (!queue.isEmpty()) {
			temp = queue.poll();
			int r = temp[0];
			int c = temp[1];
			int w = temp[2];
			
			// 밑이 벽임
			if (r + dx[d] < 0 || r + dx[d] >= L || c + dy[d] < 0 || c + dy[d] >= L || arr[r + dx[d]][c + dy[d]] == 2) {
				return false;
			} 
			// 밑에 다른 기사 있음
			if (ok(r + dx[d], c + dy[d]) && !visited[r + dx[d]][c + dy[d]] && knight[r + dx[d]][c + dy[d]] != 0) {
				temp[0] = r + dx[d];
				temp[1] = c + dy[d];
				temp[2] = knight[r + dx[d]][c + dy[d]];
				visited[temp[0]][temp[1]] = true;
				queue.add(new int[] {r + dx[d], c + dy[d], knight[r + dx[d]][c + dy[d]]});
				list.add(new int[] {r + dx[d], c + dy[d], knight[r + dx[d]][c + dy[d]]});
			}
			// 양옆 칸도 내 칸임
			if (ok(r + dx[(d + 1) % 4], c + dy[(d + 1) % 4]) && !visited[r + dx[(d + 1) % 4]][c + dy[(d + 1) % 4]] && knight[r + dx[(d + 1) % 4]][c + dy[(d + 1) % 4]] == w) {
				temp[0] = r + dx[(d + 1) % 4];
				temp[1] = c + dy[(d + 1) % 4];
				temp[2] = w;
				visited[temp[0]][temp[1]] = true;
				queue.add(new int[] {r + dx[(d + 1) % 4], c + dy[(d + 1) % 4], w});
				list.add(new int[] {r + dx[(d + 1) % 4], c + dy[(d + 1) % 4], w});
			}
			if (ok(r + dx[(d + 3) % 4], c + dy[(d + 3) % 4]) && !visited[r + dx[(d + 3) % 4]][c + dy[(d + 3) % 4]] && knight[r + dx[(d + 3) % 4]][c + dy[(d + 3) % 4]] == w) {
				temp[0] = r + dx[(d + 3) % 4];
				temp[1] = c + dy[(d + 3) % 4];
				temp[2] = w;
				visited[temp[0]][temp[1]] = true;
				queue.add(new int[] {r + dx[(d + 3) % 4], c + dy[(d + 3) % 4], w});
				list.add(new int[] {r + dx[(d + 3) % 4], c + dy[(d + 3) % 4], w});
			}
			
			// 맞은편 칸까지 보긴 해야함
			if (ok(r + dx[(d + 2) % 4], c + dy[(d + 2) % 4]) && !visited[r + dx[(d + 2) % 4]][c + dy[(d + 2) % 4]] && knight[r + dx[(d + 2) % 4]][c + dy[(d + 2) % 4]] == w) {
				temp[0] = r + dx[(d + 2) % 4];
				temp[1] = c + dy[(d + 2) % 4];
				temp[2] = w;
				visited[temp[0]][temp[1]] = true;
				queue.add(new int[] {r + dx[(d + 2) % 4], c + dy[(d + 2) % 4], w});
				list.add(new int[] {r + dx[(d + 2) % 4], c + dy[(d + 2) % 4], w});
			}
		}
		
		return true;
	}
	
    public static void main(String[] args) throws Exception {
    
    	BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
    	
    	StringTokenizer st = new StringTokenizer(br.readLine());
    	// 체스판 크기
    	L = Integer.parseInt(st.nextToken());
    	// 기사 수
    	int N = Integer.parseInt(st.nextToken());
    	// 명령 수
    	int Q = Integer.parseInt(st.nextToken());
    	
    	// 체스판 정보
    	// 0: 빈칸
    	// 1: 함정
    	// 2 : 벽
    	arr = new int[L][L];
    	for (int i = 0; i < L; i++) {
    		st = new StringTokenizer(br.readLine());
    		for (int j = 0; j < L; j++) {
    			arr[i][j] = Integer.parseInt(st.nextToken());
    		}
    	}
    	
    	// 기사들 정보
    	knight = new int[L][L];
    	// 기사 r, c 정보
    	rc = new int[N + 1][2];
    	// 기사 체력 정보
    	strength = new int[N + 1];
    	
    	for (int i = 1; i < N + 1; i++) {
    		// 처음 위치
    		st = new StringTokenizer(br.readLine());
    		int r = Integer.parseInt(st.nextToken());
    		int c = Integer.parseInt(st.nextToken());
    		// 세로, 가로 길이
    		int h = Integer.parseInt(st.nextToken());
    		int w = Integer.parseInt(st.nextToken());
    		// 초기 체력
    		int k = Integer.parseInt(st.nextToken());
    		
    		rc[i][0] = r - 1;
    		rc[i][1] = c - 1;

    		for (int a = r - 1; a < r + h - 1; a++) {
    			for (int b = c - 1; b < c + w - 1; b++) {
    				knight[a][b] = i;
    			}
    		}
    		strength[i] = k;
    	}
 
    	damages = new int[N + 1];
    	
    	for (int i = 1; i < Q + 1; i++) {
    		st = new StringTokenizer(br.readLine());
    		int n = Integer.parseInt(st.nextToken());
    		int d = Integer.parseInt(st.nextToken());

    		if (strength[n] > 0) {
        		order(n, d);
    		}
    	}
    	
    	int ans = 0;
    	for (int i = 1; i < N + 1; i++) {
    		if (strength[i] > 0) {
    			ans += damages[i];
    		}
    	}
    	System.out.println(ans);
    }
}