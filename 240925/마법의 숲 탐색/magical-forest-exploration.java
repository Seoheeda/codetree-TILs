import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.Queue;
import java.util.StringTokenizer;

// 정령 이동에 쓰이는 클래스
class Point {
	int a;
	int b;
	int cur;
	
	public Point(int a, int b, int cur) {
		super();
		this.a = a;
		this.b = b;
		this.cur = cur;
	}	
}

public class Main {
	
	static int R, C;
	static int[][] arr;
	static int way, ans;
	static int[] dx = {0, 1, 0, -1};
	static int[] dy = {1, 0, -1, 0};
	
	// 골룸 이동
	static void move(int start, int end, int fill) {
				
		// 정령 위치
		int jx = 1;
		int jy = start;
		
		// 출구
		way = end;
		
		// 골룸 위치와 출구 표시
		// 출구는 1, 나머지는 2
		arr[0][start] = fill;
		arr[1][start] = fill;
		arr[2][start] = fill;
		arr[1][start - 1] = fill;
		arr[1][start + 1] = fill;
		if (end == 0) {
			arr[0][start] = fill * -1;
		} else if (end == 1) {
			arr[1][start + 1] = fill * -1;
		} else if (end == 2) {
			arr[2][start] = fill * -1;
		} else {
			arr[1][start - 1] = fill * -1;
		}
		
		
		while (true) {
			
			// 아래로 내려가기 가능
			if (down(jx, jy, way, fill)) {
				jx = jx + 1;
			// 아래로 불가능, 서쪽으로 가능
			} else if (left(jx, jy, way, fill)) {
				jx = jx + 1;
				jy = jy - 1;
			// 아래로 불가능, 서쪽으로 불가능, 동쪽으로 가능
			} else if (right(jx, jy, way, fill)) {
				jx = jx + 1;
				jy = jy + 1;
			// 다 불가능
			} else {
				// 숲에 벗어났으면 초기화
				if (!isIn(jx, jy)) {
					arr = new int[R + 3][C];
				} else {
					// 안 벗어났으면 정령 행 누적
					ans += bfs(jx, jy, fill);
				}
				
				// for (int i = 0; i < R + 3; i++) {
				// 	System.out.println(Arrays.toString(arr[i]));
				// }
				// System.out.println();
				
				return;
			}
		}		
	}
	
	// 골룸이 숲에 벗어났는지
	static boolean isIn(int x, int y) {
		if (x - 1 >= 3) {
			return true;
		} else {
			return false;
		}
	}
	
	// 골룸 아래로 이동
	// 중앙 x, y
	// 출구 방향 w
	static boolean down(int x, int y, int w, int fill) {
		
		// 이동 가능한 arr 범위인가
		if (x + 2 < R + 3) {
			// 움직이려는 방향에 골룸이 없는가
			if (arr[x + 2][y] == 0 && arr[x + 1][y - 1] == 0 && arr[x + 1][y + 1] == 0) {
				// 기존 위치 0으로 만들기
				arr[x][y] = 0;
				arr[x - 1][y] = 0;
				arr[x][y - 1] = 0;
				arr[x][y + 1] = 0;
				arr[x + 1][y] = 0;
				// 새로운 위치 2로 만들기
				arr[x][y] = fill;
				arr[x + 1][y] = fill;
				arr[x + 2][y] = fill;
				arr[x + 1][y - 1] = fill;
				arr[x + 1][y + 1] = fill;
				// 방향 그대로 설정
				if (w == 0) {
					arr[x][y] = fill * -1;
				} else if (w == 1) {
					arr[x + 1][y + 1] = fill * -1;
				} else if (w == 2) {
					arr[x + 2][y] = fill * -1;
				} else {
					arr[x + 1][y - 1] = fill * -1;
				}
				
				return true;
			}
		}
		return false;
	}
	
	
	// 골룸 서쪽으로 회전
	// 중앙 x, y
	// 출구 방향 w
	static boolean left(int x, int y, int w, int fill) {
		
		// 회전 가능한 arr 범위인가
		if (y - 2 >= 0 && x + 2 < R + 3) {
			// 움직이려는 방향에 골룸이 없는가
			if (arr[x - 1][y - 1] == 0 && arr[x][y - 2] == 0 && arr[x + 1][y - 1] == 0 && arr[x + 1][y - 2] == 0 && arr[x + 2][y - 1] == 0) {
				// 기존 위치 0으로 만들기
				arr[x][y] = 0;
				arr[x - 1][y] = 0;
				arr[x][y - 1] = 0;
				arr[x][y + 1] = 0;
				arr[x + 1][y] = 0;
				// 새로운 위치 2로 만들기
				arr[x][y - 1] = fill;
				arr[x + 1][y] = fill;
				arr[x + 1][y - 1] = fill;
				arr[x + 1][y - 2] = fill;
				arr[x + 2][y - 1] = fill;
				// 방향 재설정
				if (w == 0) {
					arr[x + 1][y - 2] = fill * -1;
					way = 3;
				} else if (w == 1) {
					arr[x][y - 1] = fill * -1;
					way = 0;
				} else if (w == 2) {
					arr[x + 1][y] = fill * -1;
					way = 1;
				} else {
					arr[x + 2][y - 1] = fill * -1;
					way = 2;
				}
				
				return true;
				
			}
		}
		return false;
		
	}
	
	// 골룸 동쪽으로 회전
	// 중앙 x, y
	// 출구 방향 w
	static boolean right(int x, int y, int w, int fill) {
		
		// 회전 가능한 arr 범위인가
		if (y + 2 < C && x + 2 < R + 3) {
			// 움직이려는 방향에 골룸이 없는가
			if (arr[x - 1][y + 1] == 0 && arr[x][y + 2] == 0 && arr[x + 1][y + 1] == 0 && arr[x + 1][y + 2] == 0 && arr[x + 2][y + 1] == 0) {
				// 기존 위치 0으로 만들기
				arr[x][y] = 0;
				arr[x - 1][y] = 0;
				arr[x][y - 1] = 0;
				arr[x][y + 1] = 0;
				arr[x + 1][y] = 0;
				// 새로운 위치 2로 만들기
				arr[x][y + 1] = fill;
				arr[x + 1][y] = fill;
				arr[x + 1][y + 1] = fill;
				arr[x + 1][y + 2] = fill;
				arr[x + 2][y + 1] = fill;
				// 방향 재설정
				if (w == 0) {
					arr[x + 1][y + 2] = fill * -1;
					way = 1;
				} else if (w == 1) {
					arr[x + 2][y + 1] = fill * -1;
					way = 2;
				} else if (w == 2) {
					arr[x + 1][y] = fill * -1;
					way = 3;
				} else {
					arr[x][y + 1] = fill * -1;
					way = 0;
				}
				
				return true;
				
			}
		}
		return false;
		
	}
	
	static int bfs(int x, int y, int fill) {
		
		boolean[][] visited = new boolean[R + 3][C];
		
		// 최종 위치 초기화
		int dest = 0;
		int destY = 0;
		
		Queue<Point> queue = new ArrayDeque<Point>();
		// x좌표, y좌표, 출구 여부, 현재 골룸 번호
		queue.add(new Point(x, y, fill));
		visited[x][y] = true;
		
		while (!queue.isEmpty()) {
			Point p = queue.poll();
			
			if (dest < p.a) {
				dest = p.a;
				destY = p.b;
			}
			
			for (int i = 0; i < 4; i++) {
				int nx = p.a + dx[i];
				int ny = p.b + dy[i];
				fill = p.cur;
				// 이동 가능한 범위인가
				if (nx >= 0 && nx < R + 3 && ny >= 0 && ny < C && visited[nx][ny] == false) {
					// 출구에서 다른 골룸으로 이동
					if (arr[p.a][p.b] == fill * -1 && arr[nx][ny] > 0) {
						queue.add(new Point(nx, ny, arr[nx][ny]));
						visited[nx][ny] = true;
					// 골룸 내 출구로 이동
					} else if (arr[nx][ny] == fill * -1) {
						queue.add(new Point(nx, ny, fill));
						visited[nx][ny] = true;
					// 골룸 내 이동
					} else if (arr[p.a][p.b] == arr[nx][ny]) {
						queue.add(new Point(nx, ny, fill));
						visited[nx][ny] = true;
					// 출구에서 출구로 이동
					} else if (arr[p.a][p.b] < 0 &&  arr[nx][ny] < 0) {
						queue.add(new Point(nx, ny, arr[nx][ny] * -1));
						visited[nx][ny] = true;
					}
				}
			}
		}
		
		return dest - 2;
	}

    public static void main(String[] args) throws Exception {
    
    	BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
    	
    	StringTokenizer st = new StringTokenizer(br.readLine());
    	// 숲의 크기
    	R = Integer.parseInt(st.nextToken());
    	C = Integer.parseInt(st.nextToken());
    	// 정령의 수
    	int K = Integer.parseInt(st.nextToken());
    	
    	// 출발 열, 출구 방향 정보
    	// 방향 0, 1, 2, 3 : 북, 동, 남, 서
    	int[][] infos = new int[K][2];
    	for (int i = 0; i < K; i++) {
    		st = new StringTokenizer(br.readLine());
    		infos[i][0] = Integer.parseInt(st.nextToken()) - 1;
    		infos[i][1] = Integer.parseInt(st.nextToken());
    	}
    	
    	// 마법의 숲
    	arr = new int[R + 3][C];
    	
    	ans = 0;
    	
    	// 정령 순서대로
    	for (int i = 0; i < K; i++) {
    		move(infos[i][0], infos[i][1], i + 1);
    	}
    	
    	System.out.println(ans);
    }
}