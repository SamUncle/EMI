package com.icedcap.QJMeter;


public class DataBuffer {
	byte[] a; //�������飬�������洢a.length-1������  
    int front;  //�����±�  
    int rear;   //��β�±�  
    int cnt;    //���еĸ���
    
    public DataBuffer(int size){  
        a = new byte[size];  
        front = 0;  
        rear =0;  
        cnt = 0;
    }
    
    /** 
     * ��һ������׷�ӵ�����β�� 
     * @param data���飬len���鳤�� 
     * @return ������ʱ����false,���򷵻�true 
     */  
    public int enqueue(byte[] data,int len){  
    	
    	int i;
    	
    	for ( i=0;i<len;i++)
    	{
    		if((rear+1)%a.length==front)
    		{  
    			return i;  
    		}  
    		a[rear]=data[i];  
    		rear = (rear+1)%a.length;
    		cnt++;
    	}
        return i;  
    }  
    
    /** 
     * �Ӷ���ͷ����ʼ����len���ֽ� 
     * @return �������ֽ�����
     */  
    public int dequeue(byte[] data_out,int len){  
        if(rear==front){  
            return 0;  
        }  
        
        int i;
        for (i=0;i<len;i++)
        {
        	data_out[i] = a[front];  
        	front = (front+1)%a.length;
        	cnt--;
        	if (rear==front)
        	{
        		return i;
        	}
        }
       	return i;  
    }
    
    /**
     *��ȡ�ֽڳ���
     */
    public int getSize(){
		return cnt;
    }

    
}

