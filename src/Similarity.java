import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.stream.Stream;

public class Similarity {
	
	public static int[][] knowledgeBase = {
			{0,1,1,0,0}, //Schere
			{0,0,0,0,0}, //Stein
			{1,1,1,1,1}  //Papier
	};
	
	public static double[] simpleMatchingCoefficient(int[] testData){
		
		double smc[] = new double[knowledgeBase.length];
		for(int k=0; k<knowledgeBase.length; k++){
			
			int a=0,b=0,c=0,d=0;
			for(int i=0; i<5; i++){
				if(testData[i] == 1 && knowledgeBase[k][i] == 1) a++;
				if(testData[i] == 1 && knowledgeBase[k][i] == 0) b++;
				if(testData[i] == 0 && knowledgeBase[k][i] == 1) c++;
				if(testData[i] == 0 && knowledgeBase[k][i] == 0) d++;
			}
			smc[k] = (double) (a+d)/(a+b+c+d);
		}
		
		return smc;
	}
	
	public static void main(String [] args){
		//create output csv file
		String header = "Thumb,Index,Middle,Ring,Pinky,Schere,Stein,Papier";
        SampleListener.CreateCSVFile(header);
		
        //read input csv file
  		ArrayList<int []> lines = ReadFromCSVFile("kbs_assignment_ssp_input_perfect_data_2016-12-08_04.20.csv");
      		
		for(int [] testData : lines){
			
			String outputLine = "";

			//compare testData to the knowledge base
			double smc[] = simpleMatchingCoefficient(testData);
			
			//add results to output
			for(int f : testData)
				outputLine += f + ",";
			for(int i=0; i<knowledgeBase.length; i++){
				outputLine += smc[i] + ",";
			}
			outputLine = outputLine.substring(0, outputLine.length()-1);
			outputLine += "\r\n";
						
	        SampleListener.AppendRowIntoCSVFile(outputLine);
		}

        SampleListener.CloseCSVFile();
	}
	
	static ArrayList<int[]> ReadFromCSVFile(String fileName)
    {
    	ArrayList<int[]> lines = new ArrayList<int[]>();
    	String dirName = System.getProperty("user.dir");
    	try (Stream<String> stream = Files.lines(Paths.get(dirName + "//" + fileName))) {
            stream.forEach(line ->
            {
            	int [] fingers = new int[5];
            	String [] strLine = line.split(",");
            	for(int i=6; i<11; i++)
            		try{
            			fingers[i-6] = Integer.parseInt(strLine[i]);
                    	lines.add(fingers);
            		}catch(NumberFormatException e){
            			//will error on header line
            			//e.printStackTrace();
            		}
            });
    	} catch (IOException e) {
			e.printStackTrace();
		}
    	return lines;
    }

}
