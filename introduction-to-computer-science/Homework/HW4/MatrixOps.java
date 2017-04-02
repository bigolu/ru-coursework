public class MatrixOps
{
	public static double[][] multiply(double[][] matrix1, double[][] matrix2)
	{
		double sum = 0.0;
		int j = 0;
		int k = 0;
		int i = 0;
		int a = 0;
		double[][] product = new double[matrix1.length][matrix1.length];
		
		if (matrix1[0].length != matrix2.length)
			return (null);
		
		for (;j<matrix1.length;j++){
			
			for(a=0,i=0;i<matrix1.length;i++,a++){
				sum = 0;
				for (k=0;k<matrix1[0].length;k++){
				sum += (matrix1[j][k]*matrix2[k][a]);
				}	
				product[j][a] = sum;
			}
		}
		return (product);	
	}
}
