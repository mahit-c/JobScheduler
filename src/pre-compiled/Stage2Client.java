import java.net.*;
import java.io.*;
import java.util.Arrays;

class Stage2Client {
    public static void main(String args[]) throws Exception {
    
    	//Initial connection and communication establishment:
        Socket s = new Socket("127.0.0.1", 50000);
        BufferedReader in = new BufferedReader(new InputStreamReader(s.getInputStream()));
        PrintWriter out = new PrintWriter(s.getOutputStream(), true);
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

        out.println("HELO"); // Client sends HELO
        String sreply = in.readLine(); //Storing server responses //Server should respond with OK here
	
        String username = "mahit";
        String authMessage = "AUTH " + username;
        out.println(authMessage); // Client sends AUTH username
 
       	sreply = in.readLine(); //Server should respond OK
        
    	String serverToSend = null; //Storing server to schedule to type
    	String serverID=null; //Storing server to schedule to ID
    	
     
       while (true) {
      
         out.println("REDY"); // Send REDY to request for job
         sreply = in.readLine(); // Receive a message
        
      		if (sreply.equals("NONE")){ //Break out of the loop if no jobs available
		   break;
		}
	
		else if (sreply.startsWith("JCPL")) { //Next iteration of loop after job completion message
		  continue;
		}
	
		else { //Server response is JOBN; Start scheduling process + determine largest server (only first loop iteration)
	
			//Store job details:
			  String[] jobInfo = sreply.split("\\s+");
			 // System.out.println("JOBINFO ARRAY: " + jobInfo[0]);
		          int jobID = Integer.parseInt(jobInfo[2]); //job ID is the only important value needed for scheduling
		          int core = Integer.parseInt(jobInfo[4]);
		          int memory = Integer.parseInt(jobInfo[5]);
		          int disk = Integer.parseInt(jobInfo[6]);
		          
		   	  boolean check= false; //Variable to control GETS Capable scheduling
                  
            		 //Check currently available servers using GETS Avail:
             
			 out.println("GETS Avail " + core + " "+ memory + " "+ disk); 
			 sreply=in.readLine(); //Should respond with DATA X Y
			 
			
			 //System.out.println("Server response to GETS AVAIL:" + sreply);
			 out.println("OK"); //Send OK 
			 
			 String[] parts = sreply.split("\\s+"); //Storing DATA X Y response to determine nRecs and recSize
		
		         int nRecs = Integer.parseInt(parts[1]); 
		         int recSize = Integer.parseInt(parts[2]);
		         
		          if (nRecs==0) { //If NO Available servers go to servers that have jobs waiting
		          	
		      	
			 	sreply=in.readLine();
			 	out.println("OK"); //Send OK 
			 	sreply=in.readLine(); 
			 	//System.out.println("Server reply after OK = " + sreply); //Should be . here
			 	
				//Requesting list of capable servers:
				
			 	out.println("GETS Capable " + core + " "+ memory + " "+ disk); 
			 	sreply = in.readLine();
			 	sreply = in.readLine();
			 	//System.out.println( "Reply after GETS Capable: " + sreply);
			 	out.println("OK"); //Send OK after DATA X Y
			 	
			 	//Updating server list previously found:
			 	String [] newParts = sreply.split("\\s+");
			 	nRecs = Integer.parseInt(newParts[1]); //Updating value of nRecs
		 	
			 	//Setting check to true:
			 	check =  true;
			 } 
		         
				for (int i=0; i<nRecs; i++) {
					
			     	    int smallestValue=0; //for storing no.of waiting jobs
			     	    
				    sreply = in.readLine(); //Receiving each record
				    String[] serverInfo = sreply.split("\\s+");
				    
				    if (i==0) {
				    serverToSend = serverInfo[0];
				    serverID = serverInfo[1];
				    smallestValue = Integer.parseInt(serverInfo[7]); //Storing no.of waiting jobs
				  //System.out.println("The first server to schedule is: " + serverToSend +   serverID + Integer.parseInt(serverInfo[7]));
				    }
				    
				    
				    if (check) {//If checking the CAPABLE servers ; SCHEDULING TO THE SERVER WITH THE LEAST AMOUNT OF WAITING JOBS
				    
				    	if (Integer.parseInt(serverInfo[7]) < smallestValue){ 
				    	
				    		smallestValue = Integer.parseInt(serverInfo[7]);
				    		 serverToSend = serverInfo[0]; //Updating server to the one with LEAST watiitng jobs
					    	serverID = serverInfo[1];
					    	check = false;
				    	}
				    }
				    
				 }
                   
		    		out.println("OK"); //Send OK
			        sreply= in.readLine();
	

		//Scheduling jobs:
		out.println("SCHD " + jobID + " "+ serverToSend + " "+ serverID);
		
		//System.out.println("SCHD " + jobID + " "+ serverToSend + " "+ serverID); //Checking job assigment and server ID's
		
		sreply= in.readLine();
		
		//System.out.println("Server response after SCHD " + sreply);
		
	     }
	     
	  }
	  
	
      
        out.println("QUIT"); // Send QUIT to server
    
        sreply = in.readLine(); // Receive QUIT from server

        in.close();
        out.close();
        s.close();
        
     }
     
    }
    
   
   
    

  
    
    
  



