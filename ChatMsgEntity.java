package com.example.ccy.assistant;


public class ChatMsgEntity {
	private String name;
	private String date;
	private String text;
	private boolean isComMsg ;
	
	public String getName(){
		return name;
	}
	public void setName(String name){
		this.name = name;
	}
	public String getText(){
		return text;
	}
	public void setText(String text){
		this.text = text;
	}
	public String getDate(){
		return date;
	}
	public void setDate(String date){
		this.date = date;
	}
	public int getMsgType(){
		if(isComMsg)
		{
			return 1;
		}
		return 0;
	}
	public void setMsgType(boolean isComMsg){
		this.isComMsg = isComMsg;
	}

	public ChatMsgEntity(){
		
	}
	



}
