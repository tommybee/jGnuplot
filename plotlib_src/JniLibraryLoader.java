//package com.tobesoft.xup.util.classloader;
package com.tobee.iplot.classloader;

import java.io.File;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import java.util.logging.Level;
import java.util.logging.Logger;


public class JniLibraryLoader {
	
    public static void loadLibrary(String libraryPath, String libraryName) throws Exception {
			addLibraryPath(libraryPath);
			try {
				JniLibraryLoaderOfXuggle.loadLibrary(libraryName);
			} catch(UnsatisfiedLinkError e) {
				throw new Exception("library loading failed. libraryName="+libraryName, e);
			}
    }

	private static void addLibraryPath(String libraryPath) {
		if(libraryPath==null||libraryPath.equals("")) { return; }

		String javaLibraryPath = System.getProperty("java.library.path");
		
		//System.out.println(javaLibraryPath);
		
		if(javaLibraryPath.indexOf(libraryPath)==-1) {
			String osType = System.getProperty("os.name");
			//System.out.println("os -> " + osType);
			if(osType.startsWith("Windows"))
			{
				javaLibraryPath += ";"+libraryPath;
			}
			else if(osType.startsWith("Linux"))
			{
				javaLibraryPath += ":"+libraryPath;
			}
			
			//javaLibraryPath += "D:\\MyProject\\SpaceElectricWave\\DEV\\plot\\jGnuPlot\\bin"+";";
			//System.setProperty("java.library.path", javaLibraryPath);
			
			//System.out.println("--->" + libraryPath);
			//System.out.println("--->" + javaLibraryPath);
		}
		
		javaLibraryPath = System.getProperty("java.library.path");
		
		//System.out.println(javaLibraryPath);
	}
	
}


/**
* Finds and loads native libraries that we depend on.
* <p>
*   It supplements
* the Java builtin {@link System#loadLibrary(String)} by looking in more
* places.
* </p>
* <p>
* Many methods in this class are marked as "package level".  In reality, they are
* private, but are at this protection level so our test suite can test internals.
* </p>
* 
*/
class JniLibraryLoaderOfXuggle
{
	private class LogDelegator {

		public void trace(String string) {
			if(!isTraceEnabled()) { return; }
			logger.log(Level.INFO,string);
		}

		public void trace(String string, Object[] objects) {
			if(!isTraceEnabled()) { return; }
			logger.log(Level.INFO, createLogmessage(string, objects));
		}

		public void warn(String string, Object[] objects) {
			if(!isWarnEnabled()) { return; }
			logger.log(Level.WARNING, createLogmessage(string, objects));
		}

		public void trace(String string, String envVar, String pathVar) {
			if(!isTraceEnabled()) { return; }
			trace(string, new Object[] { envVar, pathVar });
		}

		public void trace(String string, String pathVar) {
			if(!isTraceEnabled()) { return; }
			trace(string, new Object[]{ pathVar });
		}

		public void trace(String string, int n, String pathVar) {
			if(!isTraceEnabled()) { return; }
			trace(string, new Object[] { n, pathVar });
		}

		public void trace(String string, OSName mos) {
			if(!isTraceEnabled()) { return; }
			trace(string, new Object[] { mos });		
		}


		public String createLogmessage(String messageTemplate, Object[] values) {
			
			//log.trace("Parsing path var: {}", aPathVar);
			/*
			int valueIndex = 0;
			String replacedValue = null;
			while(messageTemplate.indexOf("{}")>=0) {
				if(valueIndex>=values.length) { break; }
				replacedValue = values[valueIndex]==null ? null : values[valueIndex]+"";
				messageTemplate.replaceFirst("{}", replacedValue);
			}
			*/
			return messageTemplate;
			
		}

		public boolean isDebugEnabled() {
			return false;
			//return logger.isLoggable(Level.INFO);
		}

		public boolean isTraceEnabled() {
			return false;
			//return logger.isLoggable(Level.INFO);
			//return logger.isTraceEnabled();
		}

		public boolean isWarnEnabled() {
			return false;
			//return logger.isLoggable(Level.WARNING);
			//return logger.isWarnEnabled();
		}

	}
	//  private static final LogDelegator log = LoggerFactory.getLogger(JniLibraryLoaderOfXuggle.class);
	private final LogDelegator log = new LogDelegator();
	private Logger logger = null;

	/**
	 * An enumeration of the types of OS's we will do special
	 * handling for.
	 */
	enum OSName
	{
		Unknown,
		Windows,
		MacOSX,
		Linux
	};

	/**
	 * Attempts to find and load the given library, with the given version of the library if version is asked for.
	 * <p>
	 * First, if we detect that we've already loaded the given library, we'll just return rather than
	 * attempt a second load (which will fail on some OSes).
	 * </p>
	 * <p>If we haven't already loaded this library, we will search in the path defined by
	 * the property java.library.path for the library,
	 * creating OS-dependent names, and using version strings.  If we can't find it in that
	 * property, we'll search through the OS-dependent shared-library specification environment
	 * variable.
	 * </p><p>
	 * If we can't find a versioned library (and one was requested) but can
	 * find an unversioned library, we'll use the unversioned library.  But we will first search
	 * all directories for a versioned library under the assumption that if you asked for
	 * a version, you care more about meeting that requirement than finding it in the first directory
	 * we run across.
	 * </p><p>
	 * If all that still fails, we'll fall back to the {@link System#loadLibrary(String)} method
	 * (for example, if we cannot guess a libtool-like convention for the OS we're on).
	 * </p><p>
	 * We assume a libtool-like library name for the shared library, but will check for common variants on that name.
	 * </p><p>
	 * Hopefully an illustration will make this all clearer.  Assume we're looking for a library named "foo" with version 1,
	 * this method will search as follows:
	 * </p><p>
	 * <table>
	 * <tr>
	 * <th>OS</th>
	 * <th>Filenames searched for (in order)</th>
	 * <th>Directories looked in (in order)</th>
	 * </tr>
	 * <tr>
	 * <td>
	 * On Windows:
	 * </td>
	 * <td>
	 * <ol>
	 *   <li>foo-1.dll</li>
	 *   <li>libfoo-1.dll</li>
	 *   <li>cygfoo-1.dll</li>
	 *   <li>foo.dll</li>
	 *   <li>libfoo.dll</li>
	 *   <li>cygfoo.dll</li>
	 * </ol>
	 * </td>
	 * <td>
	 * <ol>
	 * <li>Every directory in the java property <b>java.library.path</b></li>
	 * <li>Every directory in the environment variable <b>PATH</b></li>
	 * </ol>
	 * </td>
	 * </tr>
	 * <tr>
	 * <td>
	 * On Linux:
	 * </td>
	 * <td>
	 * <ol>
	 *   <li>libfoo.so.1</li>
	 *   <li>libfoo.so</li>
	 * </ol>
	 * </td>
	 * <td>
	 * <ol>
	 * <li>Every directory in the java property <b>java.library.path</b></li>
	 * <li>Every directory in the environment variable <b>LD_LIBRARY_PATH</b></li>
	 * </ol>
	 * </td>
	 * </tr>
	 * <tr>
	 * <td>
	 * On Apple OS-X:
	 * </td>
	 * <td>
	 * <ol>
	 *   <li>libfoo.1.dylib</li>
	 *   <li>libfoo.dylib</li>
	 * </ol>
	 * </td>
	 * <td>
	 * <ol>
	 * <li>Every directory in the java property <b>java.library.path</b></li>
	 * <li>Every directory in the environment variable <b>DYLD_LIBRARY_PATH</b></li>
	 * </ol>
	 * </td>
	 * </tr>
	 * </table>
	 * 
	 * @param aLibraryName The name of the library, without any OS-dependent adornments like "lib", ".so" or ".dll".
	 * @param aMajorVersion The major version of this library, or null if you want to take any version.
	 * 
	 * @throws UnsatisfiedLinkError If we cannot find the library after searching in all the aforementioned locations.
	 */
	public static void loadLibrary(
			String aLibraryName,
			Long aMajorVersion)
	{
		getInstance().loadLibrary0(aLibraryName, aMajorVersion);
	}


	/**
	 * Redirects to {@link #loadLibrary(String, Long)}, but leaves the version as null (not requested).
	 * 
	 * @param aLibraryName The name of the library, without any OS-dependent adornments like "lib", ".so" or ".dll".
	 */
	public static void loadLibrary(
			String aLibraryName)
	{
		loadLibrary(aLibraryName, null);
	}

	/**
	 * The singleton instance of the {@link JniLibraryLoaderOfXuggle}.  We only allow
	 * one per class-loader instance.  Technically it would be nice to ensure only
	 * one per process, but that's currently beyond my Java ken.
	 */
	private static JniLibraryLoaderOfXuggle mGlobalLoader = new JniLibraryLoaderOfXuggle();

	/**
	 * The set of directories in the java.library.path property; this is queried once
	 * per object and then cached, as the property is read-only per Sun's documentation
	 * and will not change during the lifetime of this process.
	 */
	private String[] mJavaPropPaths;

	/**
	 * The set of directories in the environment variable this OS uses to denote places
	 * where shared objects may live (e.g. LD_LIBRARY_PATH on Linux); this is queried once
	 * per object and then cached, as the property is read-only per Sun's documentation
	 * and will not change during the lifetime of this process.
	 */
	private String[] mJavaEnvPaths;

	/**
	 * What we think the Operating System we're running on is.  This is used to guess
	 * which environment variable to use for shared objects, and how to turn a library
	 * name into a fully qualified file name.
	 */
	private OSName mOS=null;

	/**
	 * A cache that maps a library name to a set of versions we've loaded.
	 */
	private Map<String, Set<Long>> mLoadedLibraries = new HashMap<String, Set<Long>>();

	/**
	 * Get the singleton instance of the {@link JniLibraryLoaderOfXuggle}.
	 * @return The singleton instance to use for all queries.
	 */
	static JniLibraryLoaderOfXuggle getInstance()
	{
		return mGlobalLoader;
	}

	/**
	 * Constructor for {@link JniLibraryLoaderOfXuggle}.  Should not be callable from outside
	 * this class so as to ensure only the singleton object can exist.
	 */
	private JniLibraryLoaderOfXuggle()
	{
		logger = Logger.getLogger("JniLibraryLoader");
		logger.fine("<init>");
	}

	/**
	 * This is the method that actually loads the library.  It maintains an object
	 * level lock, and since this class only allows a singleton object, that is a class-level
	 * lock.  That means if you're loading a library on one thread, other threads will block
	 * until it finishes.
	 * 
	 * This should be OK in general.
	 * @param aLibraryName The library name.
	 * @param aMajorVersion  The version, or null if you don't care.
	 */
	synchronized void loadLibrary0(
			String aLibraryName,
			Long aMajorVersion)
	{
		if (alreadyLoadedLibrary(aLibraryName, aMajorVersion))
			// our work is done.
			return;

		List<String> libCandidates = getLibraryCandidates(aLibraryName, aMajorVersion);
		if (libCandidates != null && libCandidates.size() > 0)
		{
			if (!loadCandidateLibrary(aLibraryName, aMajorVersion, libCandidates))
			{
				// finally, try the System.loadLibrary call
				System.loadLibrary(aLibraryName);
				// and if we get here it means we successfully loaded since no
				// exception was thrown.  Add our library to the cache.
				setLoadedLibrary(aLibraryName, aMajorVersion);
			}
		}
	}

	/**
	 * Tell the cache that we've loaded this version.
	 * 
	 * @param aLibraryName
	 * @param aMajorVersion
	 */
	void setLoadedLibrary(String aLibraryName, Long aMajorVersion)
	{
		Set<Long> foundVersions = mLoadedLibraries.get(aLibraryName);
		if (foundVersions == null)
		{
			foundVersions = new HashSet<Long>();
			mLoadedLibraries.put(aLibraryName, foundVersions);
		}
		foundVersions.add(aMajorVersion);
	}

	/**
	 * Iterates through the set of aLibCandidates until it succeeds in loading
	 * a library.  If it succeeds, it lets the cache know.
	 * @param aLibraryName The library name.
	 * @param aMajorVersion The version we want, or null if we don't care.
	 * @param aLibCandidates The set of candidates generated by {@link #getLibraryCandidates(String, Long)}
	 * @return true if we succeeded in loading a library; false otherwise
	 */
	boolean loadCandidateLibrary(String aLibraryName, Long aMajorVersion,
			List<String> aLibCandidates)
	{
		boolean retval = false;
		for(String candidate: aLibCandidates)
		{
			File candidateFile = new File(candidate);
			if (candidateFile.exists())
			{
				String absPath = candidateFile.getAbsolutePath();
				try
				{
					/*
					log.trace("Attempt: library load of library: {}; version: {}: absolute path: {}",
							new Object[]{
							aLibraryName,
							aMajorVersion == null ? "<unspecified>" : aMajorVersion.longValue(),
									absPath
					});
					*/
					// Here's where we attempt the actual load.
					System.load(absPath);
					log.trace("Success: library load of library: {}; version: {}: absolute path: {}",
							new Object[]{
							aLibraryName,
							aMajorVersion == null ? "<unspecified>" : aMajorVersion.longValue(),
									absPath
					});
					// if we got here, we loaded successfully
					setLoadedLibrary(aLibraryName, aMajorVersion);
					retval = true;
					break;
				}
				catch(UnsatisfiedLinkError e)
				{
					log.warn("Failure: library load of library: {}; version: {}: absolute path: {}; error: {}",
							new Object[]{
							aLibraryName,
							aMajorVersion == null ? "<unspecified>" : aMajorVersion.longValue(),
									absPath,
									e
					});
				}
				catch(SecurityException e)
				{
					log.warn("Failure: library load of library: {}; version: {}: absolute path: {}; error: {}",
							new Object[]{
							aLibraryName,
							aMajorVersion == null ? "<unspecified>" : aMajorVersion.longValue(),
									absPath,
									e
					});
				}
			}
		}
		return retval;
	}

	/**
	 * For a given library, and the OS we're running on, this method generates a list
	 * of potential absolute file paths that {@link #loadCandidateLibrary(String, Long, String[])}
	 * should attempt (in order) to load.  This method will not check for existence and readability
	 * of the file we're attempting to load.
	 * 
	 * @param aLibraryName The library name
	 * @param aMajorVersion The version, or null if we don't care.
	 * @return The set of absolute file paths to try.
	 */
	List<String> getLibraryCandidates(String aLibraryName, Long aMajorVersion)
	{
		List<String> retval = new LinkedList<String>();
		// Note: when done each of these variables must be set to a non-null, non empty string array
		String[] prefixes = null;
		String[] suffixes = null;
		String[] preSuffixVersions = null;
		String[] postSuffixVersions = null;

		switch(getOS())
		{
			case Unknown:
			case Linux:
				prefixes = new String[]{ "lib", "" };
				suffixes = new String[]{ ".so" };
				preSuffixVersions = new String[]{ "" };
				postSuffixVersions = (aMajorVersion == null ? new String[]{ "" } : new String[]{"."+aMajorVersion.longValue()});
				break;
			case Windows:
				prefixes = new String[]{ "lib", "", "cyg" };
				suffixes = new String[]{ ".dll" };
				preSuffixVersions = (aMajorVersion == null ? new String[]{ "" } : new String[]{"-"+aMajorVersion.longValue()});
				postSuffixVersions = new String[]{ "" };
				break;
			case MacOSX:
				prefixes = new String[]{ "lib", "" };
				suffixes = new String[]{ ".dylib" };
				preSuffixVersions = (aMajorVersion == null ? new String[]{ "" } : new String[]{"."+aMajorVersion.longValue()});
				postSuffixVersions = new String[]{ "" };
				break;
		}
		initializeSearchPaths();

		// First check the versioned paths
		if (aMajorVersion != null)
		{
			for(String directory: mJavaPropPaths)
			{
				generateFileNames(retval, directory, aLibraryName, prefixes, suffixes, preSuffixVersions, postSuffixVersions, true);
			}
			for(String directory: mJavaEnvPaths)
			{
				generateFileNames(retval, directory, aLibraryName, prefixes, suffixes, preSuffixVersions, postSuffixVersions, true);
			}
		}
		for(String directory: mJavaPropPaths)
		{
			generateFileNames(retval, directory, aLibraryName, prefixes, suffixes, preSuffixVersions, postSuffixVersions, false);
		}
		for(String directory: mJavaEnvPaths)
		{
			generateFileNames(retval, directory, aLibraryName, prefixes, suffixes, preSuffixVersions, postSuffixVersions, false);
		}
		return retval;
	}

	void generateFileNames(List<String> aResults, String aDirectory,
			String aLibraryName,
			String[] aPrefixes, String[] aSuffixes, String[] aPreSuffixVersions,
			String[] aPostSuffixVersions, boolean aIncludeVersion)
	{
		// make sure aDirectory ends with correct terminator
		String dirSeparator = File.separator;
		if (!aDirectory.endsWith(dirSeparator))
			aDirectory = aDirectory + dirSeparator;
		for(String suffix: aSuffixes)
		{
			for(String prefix: aPrefixes)
			{
				if (aIncludeVersion)
				{
					for(String preSuffixVersion: aPreSuffixVersions)
					{
						for(String postSuffixVersion : aPostSuffixVersions)
						{
							String result = aDirectory + prefix + aLibraryName + preSuffixVersion + suffix + postSuffixVersion;
							aResults.add(result);
						}
					}
				} else
				{
					String result = aDirectory + prefix + aLibraryName + suffix;
					aResults.add(result);
				}
			}
		}
	}

	/**
	 * Initialize the paths we'll search for libraries in.
	 */
	private void initializeSearchPaths()
	{
		String pathVar = null;
		if(mJavaPropPaths == null)
		{
			pathVar = System.getProperty("java.library.path", "");
			//log.trace("property java.library.path: {}", pathVar);
			mJavaPropPaths = getEntitiesFromPath(pathVar);
		}
		if (mJavaEnvPaths == null)
		{
			String envVar = getSystemRuntimeLibraryPathVar();
			pathVar = System.getenv(envVar);
			log.trace("OS environment runtime shared library path ({}): {}", envVar, pathVar);
			mJavaEnvPaths = getEntitiesFromPath(pathVar);
		}

	}

	OSName getOS()
	{
		if (mOS != null)
			return mOS;
		OSName retval = OSName.Linux;
		String osName = System.getProperty("os.name", "Linux");
		if (osName.length() > 0)
		{
			if (osName.startsWith("Windows"))
				retval = OSName.Windows;
			else if (osName.startsWith("Mac"))
				retval = OSName.MacOSX;
			else if (osName.startsWith("Linux"))
				retval = OSName.Linux;
			else
				// default everything to Linux
				retval = OSName.Linux;
		}
		mOS = retval;
		//log.trace("Detected OS: {}", mOS);
		return retval;
	}
	/**
	 * For internal use only.  This method allows tests to override the
	 * guessed OS name.
	 * @param os The OS to set
	 */
	void setOS(OSName os)
	{
		mOS = os;
	}

	String getSystemRuntimeLibraryPathVar()
	{
		String retval = "LD_LIBRARY_PATH";
		switch(getOS())
		{
			case Windows:
				retval = "PATH";
				break;
			case MacOSX:
				retval = "DYLD_LIBRARY_PATH";
				break;
			case Linux:
			case Unknown:
				break;
		}
		return retval;
	}

	String[] getEntitiesFromPath(String aPathVar)
	{
		String[] retval = null;
		String sep = File.pathSeparator;
		if (aPathVar == null || aPathVar.length() == 0)
		{
			retval = new String[1];
			retval[0] = ".";
			log.trace("Have empty path var; assuming current directory to find native libraries");
		}
		else
		{
			//log.trace("Parsing path var: {}", aPathVar);
			//System.out.println("========> " + aPathVar);
			int len = aPathVar.length();

			// find out how many paths there are
			int i = 0;
			int n = 0;
			int j = 0;

			n = 1;
			i = aPathVar.indexOf(sep);
			while (i >= 0)
			{
				n++;
				i = aPathVar.indexOf(sep, i+1);
			}

			//log.trace("Found {} paths in path var: {}", n, aPathVar);
			// Create the return array
			retval = new String[n];

			// now fill in the actual strings
			i = 0;
			n = 0;
			j = aPathVar.indexOf(sep);
			while (j>=0)
			{
				if (j-i > 0)
				{
					// a non zero entry was found
					retval[n] = aPathVar.substring(i,j);
					//log.trace("Added path {} for path var: {}", retval[n], aPathVar);
					++n;
				} else if (j-i == 0)
				{
					// someone put two path separators, with nothing in
					// between.  just assume they meant the current directory
					retval[n] = ".";
					//log.trace("Added path {} for path var: {}", retval[n], aPathVar);
					++n;
				}
				i = j+1;
				j = aPathVar.indexOf(sep, i);
			}
			// and get the last entry which should have no separator
			retval[n] = aPathVar.substring(i, len);
			log.trace("Adding last path {} for path var: {}", retval[n], aPathVar);
			if (retval[n] == null || retval[n].length() == 0)
			{
				// A malformed path with a separator at the end; we just add
				// the current directory again.
				retval[n] = ".";
				//log.trace("Faking last path {} for malformed path var: {}", retval[n], aPathVar);
			}

		}
		return retval;
	}

	/**
	 * Checks our cache to see if we've already loaded this library.
	 * 
	 * We will also detect if we've already loaded another version of this library, and
	 * log a warning, but otherwise will return false in that case.
	 * 
	 * @param aLibraryName The library name.
	 * @param aMajorVersion The version, or null if we don't care.
	 * @return true if in cache; false otherwise
	 */
	boolean alreadyLoadedLibrary(String aLibraryName, Long aMajorVersion)
	{
		boolean retval = false;
		Set<Long> foundVersions = mLoadedLibraries.get(aLibraryName);
		if (foundVersions != null)
		{
			// we found at least some versions
			if (aMajorVersion == null || foundVersions.contains(aMajorVersion))
			{
				retval = true;
			}
			else
			{
				log.warn("Attempting load of {}, version {}, but already loaded verions: {}."+
						"  We will attempt to load the specified version but behavior is undefined",
						new Object[]{
						aLibraryName,
						aMajorVersion,
						foundVersions.toArray()
				});
			}
		}
		return retval;
	}
}
