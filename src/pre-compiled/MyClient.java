import java.net.*;
import java.io.*;
import java.util.Arrays;

class MyClient {
    public static void main(String args[]) throws Exception {
        Socket s = new Socket("127.0.0.1", 50000);
        BufferedReader in = new BufferedReader(new InputStreamReader(s.getInputStream()));
        PrintWriter out = new PrintWriter(s.getOutputStream(), true);
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

        out.println("HELO"); // Send HELO

        String username = "user.name";
        String authMessage = "AUTH " + username;
        out.println(authMessage); // Send AUTH username

        String response = in.readLine();
        /*System.out.println("Server says: " + response);*/

       
        String largestServerType = null;
        int largestServerCount = 0;
        
        
       
        while (!response.equals("NONE")) {
            
   	    System.out.println("Value at start of loop: " + response);
            out.println("REDY"); // Send REDY
            response = in.readLine(); // Receive a message
            System.out.println("First while loop response after REDY: " + response);
 
            if (response.startsWith("JOBN")) {
                // Schedule a job
                
                String[] parts = response.split("\\s+");
                int jobID = Integer.parseInt(parts[2]);
                int estRunTime = Integer.parseInt(parts[3]);
                int core = Integer.parseInt(parts[4]);
                int mem = Integer.parseInt(parts[5]);
                out.println("SCHD " + jobID + " " + largestServerType + " 0"); // Schedule on largest server type
      
            } 
            
           	else {
           	
                // Identify the largest server type
                out.println("GETS All");
                // set a single gets and complete for a server at a time
                // using a for loop use a single
                response = in.readLine();
                System.out.println("Response: " + response);
           
                String[] parts = response.split("\\s+");
                
                System.out.println("Parts: " + Arrays.toString(parts));
                
                int nRecs = Integer.parseInt(parts[0]);
                int recSize = Integer.parseInt(parts[1]);
                
               
                
                out.println("OK");
                
                int maxCount = 0;
                for (int i = 0; i < nRecs; i++) {
             
                    response = in.readLine();
                    
                    if (response == null) {
                    	break;
                    }
                    
                    System.out.println("RECENT" + response);
                    String[] serverParts = response.split("\\s+");
                    String serverType = serverParts[0];
                    int serverCount = Integer.parseInt(serverParts[1]);
                    
                    	if (serverCount > maxCount) {
                        largestServerType = serverType;
                        largestServerCount = serverCount;
                        maxCount = serverCount;
                    }
                } 
              
                out.println("OK");
            }
        }

        out.println("QUIT"); // Send QUIT 
    
        response = in.readLine(); // Receive QUIT

        in.close();
        out.close();
        s.close();
    }
    
    
  


}
