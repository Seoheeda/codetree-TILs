import java.io.*;
import java.util.*;

class Misaeng implements Comparable<Misaeng>{
	int num;
	int area;
	
	public Misaeng(int num, int area) {
		super();
		this.num = num;
		this.area = area;
	}

	@Override
	public int compareTo(Misaeng o) {
		if (this.area != o.area) {
	        return Integer.compare(o.area, this.area);
	    } else {
	        return Integer.compare(this.num, o.num);
	    }
	 }

	@Override
	public String toString() {
		return "Misaeng [num=" + num + ", area=" + area + "]";
	}
}

public class Main {
	
	static int N;
	static int[] dx = {0, 0, -1, 1};
	static int[] dy = {-1, 1, 0, 0};
	
	// 범위 안인지 여부
	static boolean isIn(int x, int y) {
		if (x >= 0 && x < N && y >= 0 && y < N) {
			return true;
		}
		return false;
	}
	
	// 둘 이상으로 나뉘었는지 확인
	static boolean isMoreThanOne(int num, int[][] arr, int area) {
		
		Queue<int[]> queue = new ArrayDeque<>();
		boolean[][] visited = new boolean[N][N];
		
		int cnt = 0;
		
		for (int i = 0; i < N; i++) {
			for (int j = 0; j < N; j++) {
				if (arr[i][j] == num) {
					queue.add(new int[] {i, j});
					visited[i][j] = true;
					cnt++;
					
					while (!queue.isEmpty()) {
						int[] temp = queue.poll();
						
						for (int d = 0; d < 4; d++) {
							int x = temp[0] + dx[d];
							int y = temp[1] + dy[d];
							
							if (isIn(x, y) && !visited[x][y] && arr[x][y] == num) {
								queue.add(new int[] {x, y});
								visited[x][y] = true;
								cnt++;
							}
						}
					}
					
					
					if (cnt == area) {
						return false;
					} else {
						return true;
					}
				}
			}
		}
		
		return true;
	}
	
	// 이동할 좌측 하단 지점 찾기
	static int[] findPlace(int x1, int y1, int x2, int y2, Queue<int[]> queue, int[][] arr) {
		for (int i = 0; i < N; i++) {
			for (int j = 0; j < N; j++) {
                Queue<int[]> queue2 = new ArrayDeque<int[]>();
                
                for (int[] item : queue) {
                    queue2.add(item);
                }
                
                if (canGoIn(i, j, x1, y1, x2, y2, queue2, arr)) {
                    return new int[] {i, j};
                }
			}
		}
		return new int[] {-1, -1};
	}
	
	// 이동 가능한 좌측 하단 지점인지 확인
	static boolean canGoIn(int a, int b, int x1, int y1, int x2, int y2, Queue<int[]> queue, int[][] arr) {
		
		if (!isIn(a + x2 - x1, b + y2 - y1) ) {
			return false;
		}
		
		while (!queue.isEmpty()) {
			int[] temp = queue.poll();
						
			if (arr[temp[0] - x1 + a][temp[1] - y1 + b] != 0) {
				return false;
			}
		}
		return true;
	}
	
	// 인접 여부 확인
	static boolean isClose(int a, int b, int[][] arr) {
		
		for (int i = 0; i <  N; i++) {
			for (int j = 0; j < N; j++) {
				if (arr[i][j] == a) {
					for (int d = 0; d < 4; d++) {
						int nx = i + dx[d];
						int ny = j + dy[d];
						
						if (isIn(nx, ny) && arr[nx][ny] == b) {
							return true;
						}
					}
				}
			}
		}
		return false;
	}
	
	public static void main(String[] args) throws Exception {
		
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		StringTokenizer st = new StringTokenizer(br.readLine());
		
		// 배양 용기 크기
		N = Integer.parseInt(st.nextToken());
		// 실험 횟수
		int Q = Integer.parseInt(st.nextToken());
		
		// 배양 용기
		int[][] arr = new int[N][N];
		
		// 미생물 영역
		int[] areas  = new int[Q + 1];
		List<Misaeng> list = new ArrayList<>();
		
		for (int q = 1; q < Q + 1; q++) {
			st = new StringTokenizer(br.readLine());
			
			int r1 = Integer.parseInt(st.nextToken());
			int c1 = Integer.parseInt(st.nextToken());
			int r2 = Integer.parseInt(st.nextToken());
			int c2 = Integer.parseInt(st.nextToken());
						
			// 1. 미생물 투입
			for (int r = r1; r < r2; r++) {
				for (int c = c1; c < c2; c++) {
					
					// 기존 미생물 번호
					int originalNum = arr[r][c];
					
					// 비어있음
					if (originalNum == 0) {
						arr[r][c] = q;
						areas[q]++;
					// 이미 임자가 있는 공간임
					} else {
						areas[originalNum]--;
						
						for (int i = 0; i < list.size(); i++) {
							Misaeng temp = list.get(i);
							if (temp.num == originalNum) {
								list.remove(i);
								temp.area--;
								list.add(new Misaeng(temp.num, temp.area));
								break;
							}
						}
						
						arr[r][c] = q;
						areas[q]++;
					}
				}
			}
			
			list.add(new Misaeng(q, areas[q]));
			
			int listSize = list.size();
			
			// 둘 이상인 무리 미생물 번호 저장
			Set<Integer> toRemove = new HashSet<>();
			
			// 미생물 무리가 둘 이상으로 나뉘면 사라짐
			for (int i = 0; i < listSize; i++) {
				int num = list.get(i).num;
								
				// 둘 이상이면 없애기
			    if (areas[num] > 0 && isMoreThanOne(num, arr, areas[num])) {
					
					for (int a = 0; a < N; a++) {
						for (int b = 0; b < N; b++) {
							if (arr[a][b] == num) {
								arr[a][b] = 0;							
							}
						}
					}
					
					areas[num] = 0;
					toRemove.add(num);
				}
			}
			
			// list.removeIf(m -> toRemove.contains(m.num));


			
			// 2. 배양 용기 이동
			
			// 영역 기준으로 정렬
			Collections.sort(list);
			
			// 옮길 새로운 배양 용기
			int[][] newArr = new int[N][N];
			
			// 미생물 개수
			int cnt = list.size();
			
			for (int i = 0; i < cnt; i++) {
				Misaeng moving = list.get(i);
				
				if (areas[moving.num] > 0) {
					
					// 해당 미생물의 좌표 모두 뺴내고, 우측 상단 / 좌측 하단 좌표 얻기
					Queue<int[]> queue = new ArrayDeque<>();
					
					int x1 = N;
					int x2 = 0;
					int y1 = N;
					int y2 = 0;
					
					for (int a = 0; a < N; a++) {
						for (int b = 0; b < N; b++) {
							if (arr[a][b] == moving.num) {
								queue.add(new int[] {a, b});
								x1 = Integer.min(x1, a);
								x2 = Integer.max(x2, a);
								y1 = Integer.min(y1, b);
								y2 = Integer.max(y2, b);
							}
						}
					}
					
					// 첫번째면 좌측 하단으로 바로 옮기기
					if (i == 0) {
						
						while (!queue.isEmpty()) {
							int[] temp = queue.poll();
							newArr[temp[0] - x1][temp[1] - y1] = moving.num;
						}
					// 아니면 조건에 맞는 공간으로 옮기기
					} else {
					
						List<int[]> backup = new ArrayList<>();
						while (!queue.isEmpty()) {
						    backup.add(queue.poll());
						}

						// 배치 가능 여부 확인에 사용
						Queue<int[]> queue2 = new ArrayDeque<>(backup);
						int[] place = findPlace(x1, y1, x2, y2, queue2, newArr);

						if (place[0] != -1 && place[1] != -1) {
						    for (int[] temp : backup) {
						        newArr[temp[0] - x1 + place[0]][temp[1] - y1 + place[1]] = moving.num;
						    }
						}
					}
				}
			}		
			
			// 3. 실험 결과 기록
			int result = 0;
			
			cnt = list.size();
			
			for (int i = 0; i < cnt; i++) {
				for (int j = i + 1; j < cnt; j++) {
					int first = list.get(i).num;
					int second = list.get(j).num;
					
					// first와 second의 인접 여부 구하기
					if (isClose(first, second, newArr)) {
						result += (areas[first] * areas[second]);
					}
				}
			}
			
			System.out.println(result);
					
			for (int i = 0; i < N; i++) {
				for (int j = 0; j < N; j++) {
					arr[i][j] = newArr[i][j];
					newArr[i][j] = 0;
				}
			}
		}
	}
}
