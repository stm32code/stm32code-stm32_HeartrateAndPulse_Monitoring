#include "led.h"
#include "delay.h"
#include "sys.h"
#include "usart.h"

#include "hc05.h"
#include "usart2.h"			 	 
#include "string.h"	 
#include "bsp_exti.h"
#include "max30102.h" 
#include "myiic.h"
#include "algorithm.h"

//ALIENTEKminiSTM32开发板扩展实验 
//ATK-HC05蓝牙串口模块实验-库函数版本  
//技术支持：www.openedv.com
//广州市星翼电子科技有限公司 
//ALIENTEK战舰STM32开发板实验13
//TFTLCD显示实验  
//技术支持：www.openedv.com
//广州市星翼电子科技有限公司 
 	
extern int Move_gewei;
extern int Move_shiwei;
extern int Move_baiwei;
extern int Move_qianwei;
extern int Move_wanwei; 


char str[100];

//心率
uint32_t aun_ir_buffer[500]; //IR LED sensor data
int32_t n_ir_buffer_length;    //data length
uint32_t aun_red_buffer[500];    //Red LED sensor data
int32_t n_sp02; //SPO2 value
int8_t ch_spo2_valid;   //indicator to show if the SP02 calculation is valid
int32_t n_heart_rate;   //heart rate value
int8_t  ch_hr_valid;    //indicator to show if the heart rate calculation is valid
uint8_t uch_dummy;
uint32_t un_min, un_max, un_prev_data;  
int i;
int32_t n_brightness;
float f_temp;
u8 temp_num=0;
u8 temp[6];
u8 dis_hr=0,dis_spo2=0;
int time;	
uint8_t hc05_role=0; 

#define MAX_BRIGHTNESS 255
//函数声明
void heart(void);


//显示ATK-HC05模块的主从状态
void HC05_Role_Show(void)
{
	
	if(HC05_Get_Role()==1)printf("ROLE:Master \n");	//主机
	else printf("ROLE:Slave \n");			 		//从机
	/*4. 设置蓝牙的名称*/
	if(HC05_Set_Cmd("AT+NAME=HC-05"))printf("4 蓝牙名称设置失败!\r\n");
	else printf("4 蓝牙名称设置为 HC-05 \r\n");
	/*5. 设置蓝牙配对密码*/
	if(HC05_Set_Cmd("AT+PSWD=1234"))printf("5 蓝牙配对密码设置失败!\r\n"); //密码必须是4位
	else printf("5 蓝牙配对密码设置为 1234 \r\n");
}


int main(void)
 {	 
	 										    
}
 
void heart(void)
{
	 
        for(i=400;i<600;i++)
        {
            un_prev_data=aun_red_buffer[i-1];
            while(MAX30102_INT==1);
            max30102_FIFO_ReadBytes(REG_FIFO_DATA,temp);
			aun_red_buffer[i] =  (long)((long)((long)temp[0]&0x03)<<16) | (long)temp[1]<<8 | (long)temp[2];    // Combine values to get the actual number
			aun_ir_buffer[i] = (long)((long)((long)temp[3] & 0x03)<<16) |(long)temp[4]<<8 | (long)temp[5];   // Combine values to get the actual number
        
            if(aun_red_buffer[i]>un_prev_data)
            {
                f_temp=aun_red_buffer[i]-un_prev_data;
                f_temp/=(un_max-un_min);
                f_temp*=MAX_BRIGHTNESS;
                n_brightness-=(int)f_temp;
                if(n_brightness<0)
                    n_brightness=0;
            }
            else
            {
                f_temp=un_prev_data-aun_red_buffer[i];
                f_temp/=(un_max-un_min);
                f_temp*=MAX_BRIGHTNESS;
                n_brightness+=(int)f_temp;
                if(n_brightness>MAX_BRIGHTNESS)
                    n_brightness=MAX_BRIGHTNESS;
            }
			//send samples and calculation result to terminal program through UART
			if(ch_hr_valid == 1 && ch_spo2_valid ==1)//**/ ch_hr_valid == 1 && ch_spo2_valid ==1 && n_heart_rate<120 && n_sp02<101
			{
				dis_hr = n_heart_rate;
				dis_spo2 = n_sp02;
			}
			else
			{
				dis_hr = 0;
				dis_spo2 = 0;
			}

		}
			if(dis_hr == 0 && dis_spo2 == 0)  //**dis_hr == 0 && dis_spo2 == 0
			{
				sprintf((char *)str,"HR:--- SpO2:--- ");//**HR:--- SpO2:---
				printf("HR:--- SpO2:--- Move:%d%d%d%d%d \r\n ",Move_wanwei,Move_qianwei,Move_baiwei,Move_shiwei,Move_gewei);		
			
			}
			else{
				sprintf((char *)str,"HR:%3d SpO2:%3d ",dis_hr,dis_spo2);//**HR:%3d SpO2:%3d 
				printf("HR:%3d SpO2:%3d Move:%d%d%d%d%d \r\n ",dis_hr,dis_spo2,Move_wanwei,Move_qianwei,Move_baiwei,Move_shiwei,Move_gewei);
				sprintf(str,"%d-%d-%d%d%d%d%d ",dis_hr,dis_spo2,Move_wanwei,Move_qianwei,Move_baiwei,Move_shiwei,Move_gewei);
				u3_printf(str);
			
			}
       maxim_heart_rate_and_oxygen_saturation(aun_ir_buffer, n_ir_buffer_length, aun_red_buffer, &n_sp02, &ch_spo2_valid, &n_heart_rate, &ch_hr_valid);
		
			
			
				

}
