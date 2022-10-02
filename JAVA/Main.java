package JAVA;

public class Main {
    public static void main(String[] args) {
        Encrypt en = new Encrypt();
        Decrypt de = new Decrypt();
        if(args[0].equals("encode"))
        {
            en.encode(args[1],args[2]);
        }
        else if(args[0].equals("decode"))
        {
            System.out.println(de.getText(args[1]));
        }
    }
}
