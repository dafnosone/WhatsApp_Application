package com.example.dschat.structs;

import java.io.Serializable;
import java.util.UUID;

public class Media implements Serializable{
	public UUID ID = null;
	public long Size = 0;
	public int Current = -1;
	public int ChuncksNumber = -1;
	public String Extension = "";
	public byte[] Chunck = null;
	
	public Media(UUID id, int chuncks, long size, String extension)
	{
		ID = id;
		Current = 0;
		ChuncksNumber = chuncks;
		Size = size;
		Extension = extension;
	}
	
	public Media(UUID id, int current, int chuncks, long size, byte[] data, String extension)
	{
		ID = id;
		Current = current;
		ChuncksNumber = chuncks;
		Size = size;
		Chunck = data;
		Extension = extension;
	}
	
	@Override
	public String toString() { return ID.toString() + ", " + Size + ", " + Extension; }
}
