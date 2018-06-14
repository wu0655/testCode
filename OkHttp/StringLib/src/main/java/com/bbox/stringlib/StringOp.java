package com.bbox.stringlib;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class StringOp {

    public static void main(String args[]) {
        if (args.length < 2) {
            System.out.println("please input filename");
            return;
        }

        for(String s : args){
            System.out.println(s);
        }

        String filename = args[0];
        String abs = args[1];

        FileWriter fout = null;
        try {
            fout = new FileWriter("out.txt");
        } catch (IOException e) {
            e.printStackTrace();
        }



        File fin = new File(args[0]);
        if (fin.exists()) {
            try (BufferedReader br = new BufferedReader(new FileReader(fin))) {
                String line;
                StringBuilder temp  = new StringBuilder();
                while ((line = br.readLine()) != null) {
                    if (line.startsWith("#")) {
                        fout.write(line);
                        continue;
                    }

                    temp.append(abs);
                    temp.append('/');
                    temp.append(line);

                    fout.write(temp.toString());
                    fout.write(System.getProperty( "line.separator" ));
                    temp.delete(0,temp.length());
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }


        try {
            fout.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
