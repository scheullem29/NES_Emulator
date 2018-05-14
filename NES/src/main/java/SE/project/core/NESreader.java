package SE.project.core;

import java.io.*;

/*
 * .NES File (NES 1.0 format)
 * 
 * PART									SIZE
 * NES Header							16 bytes
 * Trainer, if present					0 or 512 bytes
 * PRG ROM data							16384 * X bytes
 * CHR ROM data, if present				8192 * Y bytes
 * PlayChoice inst-ROM, if present		not worrying about right now
 * PayChoice PROM, if present			not worrying about right now
 * Title, if present					0 or 128 bytes
 * 
 * 
 * NES HEADER (NES 1.0 format)
 * BYTES	CONTENTS
 * 0-3		0x4E45531A ("NES" <EOF>)
 * 4 		Size of PRG ROM in 16K units (X above)
 * 5		Size of CHR ROM in 8K units (Y above)
 * 6		BITS (76543210)		CONTENTS
 * 			7-4					Lower mapper number
 * 			3					1 = ignore mirroring control or mirroring bit, provides 4-screen VRAM
 * 			2					1 = 512 byte trainer to go at $7000-$71FF, stored in file before PRG data
 * 			1					1 = Battery-backed PGR RAM goes at $6000-$7FFF or other persistent memory
 * 			0					Mirroring 0 = Horizontal (vertical mode); 1 = Vertical (horizontal mode)
 * 7		BITS (76543210)		CONTENTS
 * 			7-4					Upper mapper number
 * 			3-2					if 2, then header bytes 8-15 are in NES 2.0 format
 * 			1					PlayChoice 10
 * 			0					Vs Unisystem
 * 8		Size of PRG RAM in 8k units
 * 9		BITS (76543210)		CONTENTS
 * 			7-1					0
 * 			0					Video region: 0 = NTSC; 1 = PAL
 * 10-15	0
 */

public class NESreader 
{
	private File nesFile;
	
	public NESreader()
	{
	}
	
	public NESreader(String filepath)
	{
		this.nesFile = new File(filepath);
	}
	
	public void setNESfile(String filepath)
	{
		this.nesFile = new File(filepath);
	}
	
	public void readFile(CPU nes)
	{
		try 
		{
			FileInputStream fileIn = new FileInputStream(nesFile);
			byte[] header = new byte[16];
			fileIn.read(header);
			int prgROMblocks = -1;
			int chrROMblocks = -1;
			int mapperNumber = -1;
			boolean ignoreMirroring = false;
			boolean trainer = false;
			boolean batteryBacked = false;
			int mirroring = -1;
			boolean nes2 = false;
			boolean playChoice = false;
			boolean vsUni = false;
			int prgRAM = -1;
			int videoRegion = -1;
			
                        //detects if file is an NES file (.nes)
			if(header[0] != 0x4e | header[1] != 0x45 | header[2] != 0x53 | header[3] != 0x1a)
			{
				System.out.println("Invalid NES file!");
			}
			else
			{
				prgROMblocks = header[4];
				chrROMblocks = header[5];
				mapperNumber = ((header[6] & 0xf0)>>4)|(header[7] & 0xf0);
				
				ignoreMirroring = ((header[6]&0x08)>>3)==1;
				trainer = ((header[6]&0x04)>>2)==1;
				batteryBacked = ((header[6]&0x02)>>1)==1;
				mirroring = header[6]&0x01;
				if( (header[7]&0x0c) == 2)
				{
					nes2 = true;
				}
				playChoice = ((header[7]&0x02)>>1)==1;
				vsUni = (header[7]&0x01)==1;
				prgRAM = header[8];
				videoRegion = header[9]&0x01;

				//for the moment ignoring trainer and battery backed;
                                if(mapperNumber == 0){
                                    System.out.println("File accepted");
                                }
				if(mapperNumber != 0)
				{
					System.out.println("Mapper " + mapperNumber + " not supported.");
				}
				else if(!(prgROMblocks == 2 | prgROMblocks == 4))
				{
					System.out.println("Bad PRG ROM size");
				}
				else if(chrROMblocks != 1)
				{
					System.out.println("Bad CHR size");
				}
				else if(ignoreMirroring != false)
				{
					System.out.println("Unsupported mirroring.");
				}
				else if(trainer != false)
				{
					System.out.println("Unsupported trainer.");
				}
				else if(playChoice != false)
				{
					System.out.println("PlayChoice not supported.");
				}
				else if(vsUni != false)
				{
					System.out.println("Vs Unisystem not supported.");
				}
				else if(videoRegion != 0)
				{
					System.out.println("Only NTSC supported.");
				}
				else if(prgRAM > 1)
				{
					System.out.println("PRG RAM too large.");
				}
				else
				{
					/*
					 * Mapper 0 supports either 2 or 4 K PRG RAM not the normal 8k.
					 * If the PRG ROM is 16 then 2k of PRG RAM, 32k is 4K PRG RAM.
					 * Either way the leftover space in $6000 - $7fff is filled with
					 * mirrored data. 
					 */
                                        nes.setpgrmCtr(0x8000);
					if(batteryBacked) 
					{
						int loop = (8/prgROMblocks) - 1;
						for(int a = 0; a < (0x0800 * (prgROMblocks/2)); a++)
						{
							nes.getCPUmemory()[0x6000+a] = (byte) fileIn.read();
						}
						for(int a = 0x6800; loop > 0; loop--)
						{
							for(int b = 0x6000; b < 0x6800; b++)
							{
								nes.getCPUmemory()[a++] = nes.getCPUmemory()[b];
							}
						}
						
					}
					for(int a = 0; a < (0x4000 * prgROMblocks); a++) //as is this is assuming no more than 2 blocks
					{
						nes.getCPUmemory()[0x8000+a] = (byte) fileIn.read();
					}
					
					for(int a = 0; a < (0x2000 * chrROMblocks); a++)
					{
						nes.getPPUmemory()[a] = (byte) fileIn.read();
					}
				}
			}
			
			fileIn.close();
		} 
		catch (FileNotFoundException e) 
		{
			e.printStackTrace();
		} 
		catch (IOException e) 
		{
			e.printStackTrace();
		}
	}
}

