package ObjectStream;

import java.io.File;
import java.io.Reader;

// This object output stream will write in binary data
//  For human readable data: use FileWriter with buffered writer


public class MainFile {
    public static void main(String[] args) {
        Person p=new Person("Renjitha",21);
        File f=new File("ObjFile.txt");

        WriteObject wob=new WriteObject();
        wob.write(f,p);

        ReadObject rob=new ReadObject();
        rob.read(f);




    }
}