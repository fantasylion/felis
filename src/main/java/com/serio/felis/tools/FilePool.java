package com.serio.felis.tools;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.serio.felis.hamal.exception.SqlFactoryException;


/**
 * 文件池，当一个文件过大时需要将一个文件拆成多个文件，需要通过虚拟机变量：fileOutputDir 配置输出目录
 * @author zl.shi
 *
 */
public class FilePool {
//	private static final Logger		logger					= LoggerFactory.getLogger(FilePool.class);

	public static final long BYTE_1G    = 2147483648L;
	public static final long BYTE_512MB = 536870912L;
	
	public static final long BYTE_128MB = 134217728L;
	
	/** 最大的文件大小，超过这个文件大小后需要新建一个文件去写入*/
	public static final long MAX_SIZE   = BYTE_128MB;
	
	String filePath;
	String fileName;
	String fileSuffix = ".sql";
	
	File currentFile;
	String currentPath;
	List<String> filePool = new ArrayList<String>();
	int nextIndex = 0;
	
	public FilePool() {}
	
	
	public FilePool( String fileName ) {

		filePath = System.getProperty("fileOutputDir");
		if ( filePath == null || filePath.isEmpty() ) {
			throw new SqlFactoryException("Please set fileOutputDir by vm argument");
		}
		filePath = filePath + CommonTools.getSlant();
		
		this.fileName = fileName;
		currentPath = this.nextPoolFileAbsolute();
		
	}

	
	public FilePool( String currentPath, String fileName ) {
		this.filePath = currentPath;
		this.fileName = fileName;
		currentPath = this.nextPoolFileAbsolute();
	}

	
	/**
	 * <p>获取一个文件，默认第一个文件命名规则：0为第一份文件，1为第二份文件以此类推，如果文件已经存在并超出设置的最大值大小，将会将会遵循命名规则获取下一个文件。</p>
	 * @return
	 */
	public File getOneFile() {
		
		File file = getCurrentFile();
		
		file = splitFile( file );
		
		return file;
		
	}
	
	
	/**
	 * 如果文件大小超过{@link MAX_SIZE}将会把文件进行拆分
	 * @param file
	 * @return
	 */
	public File splitFile( File file ) {
		
		if ( isNeedSplitFile( file ) ) {
			String absolute = nextPoolFileAbsolute();
			file = new File(absolute);
			createFileIfNotExist( file );
			this.currentFile = file;
		}
		
		return file;
	}
	
	
	/**
	 * 如果这个文件不存在将会创建一个新的文件
	 * @param file
	 * @return
	 */
	public File createFileIfNotExist( File file ) {
		if ( !file.exists() ) {
			try {
				File parentFile = file.getParentFile();
				if ( !parentFile.exists() ) {
					parentFile.mkdirs();
				}
				file.createNewFile();
			} catch (IOException e) {
//				logger.error("Has exception",e);
			}
		}
		return file;
	}
	
	
	/**
	 * 下一个文件的绝对路径
	 * @return
	 */
	public String nextPoolFileAbsolute() {
		
		String absolute = filePath + fileName + nextIndex + fileSuffix;
		currentPath = absolute;
		this.filePool.add(absolute);
		nextIndex++;
		
		return absolute;
	}
	
	
	/**
	 * 当前的文件
	 * @return
	 */
	private File getCurrentFile() {
		
		if ( currentFile != null ) {
			return currentFile;
		}
		
		currentFile = new File( currentPath );
		currentFile = createFileIfNotExist( currentFile );
		return currentFile;
	}
	
	
	/**
	 * 判断文件是否需要进行拆成多个，依据{@link #MAX_SIZE}对比文件大小
	 * @param file
	 * @return
	 */
	public boolean isNeedSplitFile( File file ) {
		return file.length() >= MAX_SIZE;
	}
	
	
}

