import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.StringTokenizer;

public class Main {
	
	static class Point implements Comparable<Point> {
		int x;
		int y;
		
		public Point(int x, int y) {
			this.x = x;
			this.y = y;
		}

		// 새로운 조각 생성 순서
		// 1. 열 번호가 작은 순
		// 2. 행 번호가 큰 순
		@Override
		public int compareTo(Point o) {
			if (this.y == o.y) {
				return o.x - this.x;
			}
			
			return this.y - o.y; 
		}
	}
	
	static class Center implements Comparable<Center> {
		int x;
		int y;
		int gachi;
		int rotate;
		
		public Center(int x, int y, int gachi, int rotate) {
			this.x = x;
			this.y = y;
			this.gachi = gachi;
			this.rotate = rotate;
		}
  
		// 회전 방법 선택
		// 1. 유물 획득 가치 최대화
		// 2. 회전 각도가 가장 작은
		// 3, 회전 중심 좌표 열이 가장 작은
		// 4. 회전 중심 좌표의 행이 가장 작은
		@Override
		public int compareTo(Center o) {
			if (this.gachi == o.gachi) {
				if (this.rotate == o.rotate) {
					if (this.y == o.y) {
						return this.x - o.x; 
					}
					return this.y - o.y; 
				}
				return this.rotate - o.rotate; 
			}
			return o.gachi - this.gachi; 
		}
	}
		
	
	static int[][] arr;
	static int[][] newArr;
	
	static int[] dx = {1, 0, -1, 0};
	static int[] dy = {0, 1, 0, -1};
	static Queue<Integer> pieces;
	static PriorityQueue<Point> remove;
	
	// 회전하기
	static void turn(int x, int y, int cnt) {
		
		newArr = new int[5][5];
		
		for (int i = 0; i < 5; i++) {
			for (int j = 0; j < 5; j++) {
				newArr[i][j] = arr[i][j];
			}
		}

		for (int i = 0; i < cnt; i++) {
			int start = newArr[x - 1][y - 1];
			int start2 = newArr[x][y - 1];
			newArr[x - 1][y - 1] = newArr[x + 1][y - 1];
			newArr[x][y - 1] = newArr[x + 1][y];
			newArr[x + 1][y - 1] = newArr[x + 1][y + 1];
			newArr[x + 1][y] = newArr[x][y + 1];
			newArr[x + 1][y + 1] = newArr[x - 1][y + 1];
			newArr[x][y + 1] = newArr[x - 1][y];
			newArr[x - 1][y + 1] = start;
			newArr[x - 1][y] = start2;
		}
		
	}
	
	
	static int bfs(int[][] array) {
		boolean[][] visited = new boolean[5][5];
		Queue<Point> queue = new LinkedList<>();
		remove = new PriorityQueue<>();
		
		for (int i = 0; i < 5; i++) {
			for (int j = 0; j < 5; j++) {
				if (visited[i][j] == false) {
					queue.add(new Point(i, j));
					visited[i][j] = true;
					
					ArrayList<Point> list = new ArrayList<>();
					
					int cnt = 1;
					list.add(new Point(i, j));
					
					while (!queue.isEmpty()) {
						Point point = queue.poll();
						
						for (int d = 0; d < 4; d++) {
							int nx = point.x + dx[d];
							int ny = point.y + dy[d];
								
							// 범위 벗어남
							if (nx < 0 || nx >= 5 || ny < 0 || ny >= 5) {
								continue;
							}
							
							// 이미 방문
							if (visited[nx][ny] == true) {
								continue;
							}
							
							// 시작점과 같은 유물
							if (array[nx][ny] == array[i][j]) {
								queue.add(new Point(nx, ny));
								visited[nx][ny] = true;
								
								cnt++;
								list.add(new Point(nx, ny));
							}
						}
					}
					
					// 3개 이상 연결되었으면
					if (cnt >= 3) {
						remove.addAll(list);
					}
					
				}
			}
		}
		
		// 사라질 유물 개수 반환
		return remove.size();
	}
	
	// 유물 채우기
	static void fill() {
		while (!remove.isEmpty()) {
			Point point = remove.poll();
			
			arr[point.x][point.y] = pieces.poll();  
		}
	}
	
    public static void main(String[] args) throws Exception {
    	
    	BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
    	StringTokenizer st = new StringTokenizer(br.readLine());
    	// 탐사 반복 횟수
    	int K = Integer.parseInt(st.nextToken());
    	    	
    	// 유물 조각의 개수
    	int M = Integer.parseInt(st.nextToken());
    	
    	// 5 * 5 격자
    	arr = new int[5][5];
    	for (int i = 0; i < 5; i++) {
    		st = new StringTokenizer(br.readLine());
    		for (int j = 0; j < 5; j++) {
    			arr[i][j] = Integer.parseInt(st.nextToken());
    		}
    	}
    	
    	// 유물 조각
    	 pieces = new LinkedList<>();
         st = new StringTokenizer(br.readLine());
         
         for (int i = 0; i < M; i++) {
        	 pieces.add(Integer.parseInt(st.nextToken()));
         }
         
        
         for (int t = 0; t < K; t++) {
        	 
        	 ArrayList<Center> hoobo = new ArrayList<>();
        	 
        	 // 90, 180, 270도 회전
        	 for (int cnt = 1; cnt <= 3; cnt++) {
         		for (int i = 1; i <= 3; i++) {
         			for (int j = 1; j <= 3; j++) {
         				turn(i, j, cnt);
         				int score = bfs(newArr);
         				
         				// 유물을 획득하는 경우면 후보에 추가
         				if (score > 0) {
         					hoobo.add(new Center(i, j, score, cnt));
         				}
         			}
         		}
         	}
        	 
        	 // 유물 획득 못하는 경우면 종료
        	 if (hoobo.isEmpty()) {
         		break;
         	}
        	 
        	// 후보 중, 정렬 1번째 선택
         	Collections.sort(hoobo); 
         	
         	Center good = hoobo.get(0);
         	int gx = good.x;
         	int gy = good.y;
         	int gr = good.rotate;
         	
         	turn(gx, gy, gr);
         	arr = newArr;
         	
         	int gachi = bfs(arr);
         	int sum = 0;
         	
         	// 더이상 가치가 없을 때까지
         	while (gachi > 0) {
         		fill();
         		sum += gachi;
         		
         		gachi = bfs(arr);
         	}
         	
         	if (sum == 0) {
         		break;
         	} else {
         		System.out.print(sum + " ");
         	}         	
         }  
    }
}