import java.net.*;
import java.io.*;
class MyClient{
public static void main(String args[])throws Exception{
Socket s=new Socket("localhost",3333);
//DataInputStream din=new DataInputStream(s.getInputStream());
BufferedReader in =new BufferedReader(new InputStreamReader(s.getInputStream()));
DataOutputStream dout=new DataOutputStream(s.getOutputStream());
BufferedReader br=new BufferedReader(new InputStreamReader(System.in));
String str="",str2="";
while(!str.equals("stop")){
str=br.readLine();
dout.write((HELLO\n).getBytes());
dout.flush();

String username= System.getProperty("user.name");
String authMessage= "AUTH" + username;
dout.write(authMessage.getBytes());
dout.flush();
str2=in.readLine();
System.out.println("Server says: "+str2);
}

dout.close();
s.close();
}}
