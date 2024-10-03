import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.StringTokenizer;

class Santa implements Comparable<Santa>{
	int num;
	int sR;
	int sC;
	int dead;
	int dist;
	int score;
	
	public Santa(int num, int sR, int sC, int dead, int dist, int score) {
		super();
		this.num = num;
		this.sR = sR;
		this.sC = sC;
		this.dead = dead;
		this.dist = dist;
		this.score = score;
	}

	// 산타 우선순위에 따라
	@Override
	public int compareTo(Santa o) {
		if (this.dist == o.dist) {
			if (this.sR == o.sR) {
				return Integer.compare(o.sC, this.sC);
			}
			return Integer.compare(o.sR, this.sR);
		}
		return Integer.compare(this.dist, o.dist);
	}	
}

public class Main {
	
	static int rR, rC, N, C, D;
	static List<Santa> santas;
	static int[][] arr;
	// 루돌프 8방향
	static int[] dxR = {0, -1, -1, -1, 0, 1, 1, 1};
	static int[] dyR = {-1, -1, 0, 1, 1, 1, 0, -1};
	// 산타 4방향 - 좌하우상
	static int[] dxS = {0, 1, 0, -1};
	static int[] dyS = {-1, 0, 1, 0};
	
	// 두칸 사이의 거리
	static int distance(int r1, int c1, int r2, int c2) {
		return (r1 - r2) * (r1 - r2) + (c1 - c2) * (c1 - c2);
	}
	
	// 게임판 밖인지 여부
	static boolean inIt(int x, int y) {
		if (x >= 0 && x < N && y >= 0 && y < N) {
			return true;
		} 
		return false;
	}
	
	// 루돌프 이동
	static void rudolph() {
		
		// 루돌프와 산타 가까운 순서대로 정렬하기
		for (int i = 0; i < santas.size(); i++) {
			Santa s = santas.remove(0);
			santas.add(new Santa(s.num, s.sR, s.sC, s.dead, distance(rR, rC, s.sR, s.sC), s.score));	
		}
		
		Collections.sort(santas);
		
		// 타켓 산타
		Santa target = santas.remove(0);
		System.out.println("target " + target.sR + " " + target.sC);
		
		// 8방향 증 가장 가까워지는 방향 찾기
		int minDist = Integer.MAX_VALUE;
		int way = -1;
		for (int i = 0; i < 8; i++) {
			int tempDist = distance(rR + dxR[i], rC + dyR[i], target.sR, target.sC);
			if (minDist >= tempDist) {
				minDist = tempDist;
				way = i;
			}
		}
		// 해당 방향으로 루돌프 좌표 이동
		arr[rR][rC] = 0;
		rR += dxR[way];
		rC += dyR[way];
		
		// 움직인 좌표에 산타가 있으면
		if (arr[rR][rC] > 0) {
			arr[target.sR][target.sC] = 0; 
			// 해당 산타 점수 증가
			target.score += C;
			// 해당 산타 밀림
			target.sR += (dxR[way] * C);
			target.sC += (dyR[way] * C);
			
			while (true) {
				// 밀려난 산타가 게임판 안이라면
				if (inIt(target.sR, target.sC)) {
					
					// 밀려난 곳에 다른 산타가 있다면
					if (arr[target.sR][target.sC] > 0) {
						// 해당 산타 번호
						int num = arr[target.sR][target.sC];
						// 밀려난 산타 안착
						arr[target.sR][target.sC] = target.num;
						System.out.println("넣을거 " + target.sR + " " + target.sC);
						santas.add(target);
						// 해당 산타 정보 찾기
						for (int i = 0; i < santas.size(); i++) {
							if (santas.get(i).num == num) {
								target = santas.remove(i);
							}
						}
						target.sR += dxR[way];
						target.sC += dyR[way];
					}
				} else {
					break;
				}
			}
		} else {
			santas.add(target);
		}
		
		arr[rR][rC] = -1;
	}
	
	// 산타 이동
	static void santa() {
		
		for (int i = 0; i < santas.size(); i++) {
			
			Santa s = santas.remove(0);
			System.out.println("움직일산타 " + s.sR + " " + s.sC);
			System.out.println("루돌프위치  " + rR + " " + rC);
			arr[s.sR][s.sC] = 0;
			// 현재 루돌프와의 거리
			int nowDist = distance(rR, rC, s.sR, s.sC);
			
			// 4방향 중 가장 가까워지는 방향 찾기
			int minDist = Integer.MAX_VALUE;
			int way = -1;
			for (int d = 0; d < 4; d++) {
				if (inIt(s.sR + dxS[d], s.sC + dyS[d]) && arr[s.sR + dxS[d]][s.sC + dyS[d]] <= 0) {
					int tempDist = distance(rR, rC, s.sR + dxS[d], s.sC + dyS[d]);
					if (nowDist > tempDist && minDist >= tempDist) {
						minDist = tempDist;
							way = d;
					}
				}
			}
			System.out.println("way " + way);
			
			// 움직일 수 있는 칸이 있다면
			if (way != -1) {
				
				// 게임판 안이고, 다른 산타가 없다면
				if (inIt(s.sR + dxS[way], s.sC + dyS[way]) && arr[s.sR + dxS[way]][s.sC + dyS[way]] <= 0) {
					System.out.println("ok");
					arr[s.sR][s.sC] = 0; 
					s.sR += dxS[way];
					s.sC += dyS[way];
				}
				
				// 루돌프와 충돌 일어났다면
				if (arr[s.sR][s.sC] == -1) {
					s.score += D;
					s.sR += dxS[(way + 2) % 4] * D;
					s.sC += dyS[(way + 2) % 4] * D;
					
					while (true) {
						// 밀려난 산타가 게임판 안이라면
						if (inIt(s.sR, s.sC)) {
							
							// 밀려난 곳에 다른 산타가 있다면
							if (arr[s.sR][s.sC] > 0) {
								// 해당 산타 번호
								int num = arr[s.sR][s.sC];
								// 밀려난 산타 안착
								arr[s.sR][s.sC] = s.num;
								System.out.println("넣을거 " + s.sR + " " + s.sC);
								santas.add(s);
								// 해당 산타 정보 찾기
								for (int j = 0; j < santas.size(); j++) {
									if (santas.get(j).num == num) {
										s = santas.remove(i);
									}
								}
								s.sR += dxS[(way + 2) % 4];
								s.sC += dyS[(way + 2) % 4];
							}
						} else {
							break;
						}
					}
				} else {
					arr[s.sR][s.sC] = s.num; 
					santas.add(s);
				}
			} else {
				arr[s.sR][s.sC] = s.num; 
				santas.add(s);
			}
		}
	}
	
    public static void main(String[] args) throws Exception {
    	
    	BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
    	StringTokenizer st = new StringTokenizer(br.readLine());
    	
    	// 게임판 크기
    	N = Integer.parseInt(st.nextToken());
    	// 게임 턴 수
    	int M = Integer.parseInt(st.nextToken());
    	// 산타 수
    	int P = Integer.parseInt(st.nextToken());
    	// 루돌프 힘
    	C = Integer.parseInt(st.nextToken());
    	// 산타 힘
    	D = Integer.parseInt(st.nextToken());
    	
    	// 게임판
    	arr = new int[N][N];
    	
    	st = new StringTokenizer(br.readLine());
    	// 루돌프 초기 위치 : 루돌프는 -1로 표기
    	rR = Integer.parseInt(st.nextToken()) - 1;
    	rC = Integer.parseInt(st.nextToken()) - 1;
    	arr[rR][rC] = -1;
    	
    	// 산타 번호, 초기 위치 R, 초기 위치 C, 기절, 거리
		santas = new ArrayList<Santa>();
    	for (int i = 0; i < P; i++) {
    		st = new StringTokenizer(br.readLine());
    		int n = Integer.parseInt(st.nextToken());
    		int a = Integer.parseInt(st.nextToken()) - 1;
    		int b = Integer.parseInt(st.nextToken()) - 1;
    		santas.add(new Santa(n, a, b, 0, 0, 0));
    		arr[a][b] = n;
    	}
    	
    	for (int i = 0; i < N; i++) {
    		System.out.println(Arrays.toString(arr[i]));
    	}
    	System.out.println();
    	
    	// 총 M번 게임 턴 진행
    	for (int i = 0; i < M; i++) {
    		System.out.println("----------------" + (i + 1) + "턴");
    		
    		// 루돌프 이동
    		rudolph();
//    		for (int j = 0; j < N; j++) {
//        		System.out.println(Arrays.toString(arr[j]));
//        	}
//    		System.out.println(rR + " " + rC);
    		
    		santa();
//    		for (int j = 0; j < N; j++) {
//        		System.out.println(Arrays.toString(arr[j]));
//        	}
    	}
    }
}