import java.util.Arrays;
import java.util.Scanner; 
public class SortingIntegers {

	public static void main(String[] args) {
		Scanner scanner = new Scanner(System.in); 
		
		int[] numbers = new int[10];
		
		for (int i =0; i<10;i++) {
			System.out.print("Nhập số nguyên thứ "+ i+": ");
			numbers[i] = scanner.nextInt();
		}
		
		Arrays.sort(numbers);
		System.out.println("Mảng sau khi đã sắp xếp:");
		
		for(int i=0; i<10; i++) {
			System.out.println("Số nguyên thứ "+ i+": "+numbers[i]);
		}
		
		scanner.close();
	}

}
