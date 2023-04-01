import java.net.*;
import java.io.*;
import java.util.Arrays;

class MyClient {
    public static void main(String args[]) throws Exception {
        Socket s = new Socket("127.0.0.1", 50000);
        BufferedReader in = new BufferedReader(new InputStreamReader(s.getInputStream()));
        PrintWriter out = new PrintWriter(s.getOutputStream(), true);
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

        out.println("HELO"); // Client sends HELO
	String response = in.readLine(); //Server should response OK
	
        String username = "mahit";
        String authMessage = "AUTH " + username;
        out.println(authMessage); // Client sends AUTH username

       	response = in.readLine(); //Server should respond OK
        /*System.out.println("Server says: " + response);*/

    
       	Boolean flag = true;
        String largestServerType = null;
        int largestCoreCount = 0;
        int largestServerCount = 1;
        
        int serverID = 0;
        
     
      while (true) {
      
      	 //System.out.println("Value at start of loop: " + response); //Checking server response
         out.println("REDY"); // Send REDY
         response = in.readLine(); // Receive a message
         System.out.println("First while loop response after REDY: " + response); //Checking server response
        
            
      	if (response.equals("NONE")){ //Break out of the loop if no jobs available
		break;
	}
	
	else if (response.startsWith("JCPL")) { //Next iteration of loop after job completion message
		continue;
	}
	
	else { //Server response is JOBN
	
		//Store job details:
		String[] jobInfo = response.split("\\s+");
                int jobID = Integer.parseInt(jobInfo[2]);
                int estRunTime = Integer.parseInt(jobInfo[3]);
                int core = Integer.parseInt(jobInfo[4]);
                int mem = Integer.parseInt(jobInfo[5]);
                System.out.println("Job details: " +  Arrays.toString(jobInfo));
	
		//Check server info only ONCE?:
		if (flag) {
		 out.println("GETS All"); //Get server state information
		 response=in.readLine(); //Should respond with DATA X Y
		 out.println("OK"); //Send OK 
		 
		String[] parts = response.split("\\s+"); 
                System.out.println("Parts: " + Arrays.toString(parts)); //Checking server response
                
                int nRecs = Integer.parseInt(parts[1]);
                int recSize = Integer.parseInt(parts[2]);
               
              
                for (int i = 0; i < nRecs; i++) {
             
                    response = in.readLine(); //Receiving each record
                    
                    
                    System.out.println(response); //Checking record info
                    
                    String[] serverInfo = response.split("\\s+");
                    String serverType = serverInfo[0];
                    int coreCount = Integer.parseInt(serverInfo[4]);
                    
                     if (serverType.equals(largestServerType)) { //Checking if there is more of the largest server type
                    		largestServerCount++; //Incrementing count if current server type equals to largest
        
                } 
                   
                    if (coreCount > largestCoreCount){ 	//Checking if there is a larger server type
                    	 largestServerType = serverType; //Updating largest server type 
                         largestCoreCount= coreCount; //Updating largest core count value
                         largestServerCount=1; // Resetting count 1 if new larger count is found
		}   
	     }
	     
	     out.println("OK"); //Send OK
	     response= in.readLine();
	
	}
	
	flag=false; //Determining largest server only ONCE?
	
	if (serverID >= largestServerCount){
		serverID=0; //resetting to 0
	}
	//Scheduling jobs:
	out.println("SCHD " + jobID + " "+ largestServerType + " "+ serverID);
	System.out.println("Job assigned to: " + serverID); //Checking server ID
	serverID++; //Incrementing server ID to assign next job to next server
	response= in.readLine();
	
	
	//Checking largest server info:
	System.out.println("LargServ: " + largestServerType);
	System.out.println("LargeCCount: " + largestCoreCount);
	System.out.println("SERVCOUNT: " + largestServerCount);
	
		}
	}
      
        out.println("QUIT"); // Send QUIT 
    
        response = in.readLine(); // Receive QUIT

        in.close();
        out.close();
        s.close();
   }
    
 }
  
    
    
  



