#include "jni_gnuplot.h"

#include <stdlib.h>
#include <string.h>

#include "gnuplot_i.h"

#define GP_CMD_SIZE     	2048

static gnuplot_ctrl * g_ctrl;
static char *jbyteArray2cstr(JNIEnv *env, jbyteArray javaBytes);

/*
 * Class:     com_skcc_ignuplot_jni_IGnuPlot
 * Method:    initIGnuPlotCmd
 * Signature: ()I kr.go.rra.swc.system.dps.iplot.jni.IGnuPlot
 */
JNIEXPORT jint JNICALL Java_com_tobee_iplot_jni_IGnuPlot_initIGnuPlotCmd
  (JNIEnv *jenv, jclass jcls, jstring jplotpath)
{
	char *plotpath = NULL;
	jint status = 0;
	const char *ss;
	int len = -1;
	
	ss  = (*jenv)->GetStringUTFChars(jenv, jplotpath, 0);
	len = strlen(ss) + 1;

	
	plotpath = calloc( len, sizeof(char));
	
	strcpy (plotpath, ss);
	
	/*printf("init completed...[%s]\r\n", plotpath);*/
	
	g_ctrl = gnuplot_init_with_path(plotpath);

	if(g_ctrl)
	{
		/*printf("find path %s\r\n", plotpath);*/
		status = 1;
	}
	else
	{
		status = 0;
	}
	
	/*printf("init completed...\r\n");*/
	(*jenv)->ReleaseStringUTFChars(jenv, jplotpath, ss );
	return status;
}

/*
 * Class:     com_skcc_ignuplot_jni_IGnuPlot
 * Method:    gnuPlotCmd
 * Signature: (Ljava/lang/String;)I
 */
JNIEXPORT jint JNICALL Java_com_tobee_iplot_jni_IGnuPlot_gnuPlotCmd
  (JNIEnv *env, jclass jcls, jstring cmd)
{
		char *plotscript = NULL;
		char line[GP_CMD_SIZE];
		jboolean iscopy;
    char* ptr;
    char *temp;
    char *plotline = NULL;
    int linecnt = 0;
	
		plotscript = strdup((*env)->GetStringUTFChars(env, cmd, &iscopy));
		//(*env)->ReleaseStringUTFChars(env, cmd,plotscript);
		
    if( plotscript == NULL || *plotscript == '\0')
        return 0;

    temp = strdup(plotscript);
    
    free(plotscript);
    plotscript = NULL;
     
    ptr = strtok(temp, "\n");

    while(ptr)
    {
        plotline = strdup(ptr);
        sprintf(line, "%s\n", plotline);
        /*printf(line);*/
        ptr = strtok(NULL, "\n");
        linecnt++;
 		
 				fputs(line, g_ctrl->gnucmd);   
   			fflush(g_ctrl->gnucmd);
 		       
        free(plotline); 
        plotline = NULL;
    }
    free(temp);
    temp = NULL;
	
		gnuplot_close(g_ctrl);
	
	/*printf("Done!!!!!!!!\r\n");*/
	
	return 1;
}

/*
 * Class:     kr_go_rra_swc_system_dps_iplot_jni_IGnuPlot
 * Method:    gnuPlotCmdBytes
 * Signature: ([B)V
 */
JNIEXPORT void JNICALL Java_com_tobee_iplot_jni_IGnuPlot_gnuPlotCmdBytes
(JNIEnv *env, jclass jcls, jbyteArray javaBytes)
{
	char *plotscript = NULL;
	char line[GP_CMD_SIZE];
    char* ptr;
    char *temp;
    char *plotline = NULL;
    int linecnt = 0;
	
	plotscript = jbyteArray2cstr(env, javaBytes);
		
    if( plotscript == NULL || *plotscript == '\0')
        return;
	/*printf("%s", plotscript);*/
    temp = strdup(plotscript); 
    ptr = strtok(temp, "\n");

    while(ptr)
    {
        plotline = strdup(ptr);
        sprintf(line, "%s\n", plotline);
        /*printf(line);*/
        ptr = strtok(NULL, "\n");
        linecnt++;
 		
 		fputs(line, g_ctrl->gnucmd);   
   		fflush(g_ctrl->gnucmd);
 		       
        free(plotline), plotline = NULL;
    }
    free(temp);

	free(plotscript);
	plotscript = NULL;
	
	gnuplot_close(g_ctrl);
	
	/*printf("Done!!!!!!!!\r\n");*/
	
	return;
}


char *jbyteArray2cstr(JNIEnv *env, jbyteArray javaBytes) {
    size_t len = (*env)->GetArrayLength(env, javaBytes);
    jbyte *nativeBytes = (*env)->GetByteArrayElements(env, javaBytes, 0);
    char *nativeStr = (char*)malloc(len+1);
    strncpy( nativeStr, (const char*)nativeBytes, len );
    nativeStr[len] = '\0';
    (*env)->ReleaseByteArrayElements(env, javaBytes, nativeBytes, JNI_ABORT);
    return nativeStr;
} 

/*
 * Class:     com_skcc_ignuplot_jni_IGnuPlot
 * Method:    gnuPlotCmdScript
 * Signature: (Ljava/lang/String;Ljava/lang/String;)I
 */
JNIEXPORT jint JNICALL Java_com_tobee_iplot_IGnuPlot_gnuPlotCmdScript
  (JNIEnv *jenv, jclass jcls, jstring jstrScript, jstring jstrData)
{
	return 0;
}
