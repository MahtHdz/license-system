package com.maht.license_gen;

import com.maht.id_service.IDsCollector;


public class Main {
    
    public static void main(String args[]) throws Exception {
        //KeyGen key = new KeyGen();
        String hdd = null, bios = null;
        
        //key.init_exec(); 
		
		IDsCollector IDs = new IDsCollector();
		try {
			bios = IDs.getBIOS_ID();
			hdd = IDs.getHDD_ID();
		}catch(Exception e) {
			e.printStackTrace();
		}
		System.out.println(" HDD ID  -> " + hdd +"\n BIOS ID -> " + bios);
	}
	
	static {
		System.loadLibrary("res/SysIDs");
	}
}
