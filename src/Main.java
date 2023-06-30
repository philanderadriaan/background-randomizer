import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Date;
import java.util.Properties;
import java.util.Random;

import javax.swing.JOptionPane;
import javax.swing.UIManager;

public class Main
{

  private static final Properties PROPERTIES = new Properties();

  public static void main(String[] args) throws Exception
  {
    try
    {
      UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
      PROPERTIES.load(new FileInputStream("background-randomizer.properties"));

      File destinationDirectory = getDirectory("destination.directory");
      for (File destinationFile : destinationDirectory.listFiles())
      {
        log("Delete", destinationFile.getAbsolutePath());
        destinationFile.delete();
      }

      File sourceDirectory = getDirectory("source.directory");
      File[] sourceSubDirectories = sourceDirectory.listFiles();
      File sourceSubDirectory = sourceSubDirectories[new Random().nextInt(sourceSubDirectories.length)];
      File newSourceSubDirectory = new File(sourceDirectory.getAbsolutePath() + "\\" + System.currentTimeMillis());
      log("Rename", sourceSubDirectory.getAbsolutePath(), newSourceSubDirectory.getAbsolutePath());
      sourceSubDirectory.renameTo(newSourceSubDirectory);
      sourceSubDirectory = newSourceSubDirectory;

      for (File sourceGroup : sourceSubDirectory.listFiles())
      {
        Thread.sleep(1);
        if (sourceGroup.isFile())
        {
          log("Copy", sourceGroup.getAbsolutePath(), destinationDirectory.getAbsolutePath());
          Files.copy(sourceGroup.toPath(), Paths.get(destinationDirectory.getAbsolutePath() + "\\" + sourceGroup.getName()));
        }
        else if (sourceGroup.isDirectory())
        {
          File newSourceGroup = new File(sourceSubDirectory.getAbsolutePath() + "\\" + (char) (new Random().nextInt('Z' - 'A') + 'A') + System.currentTimeMillis());
          log("Rename", sourceGroup.getAbsolutePath(), newSourceGroup.getAbsolutePath());
          sourceGroup.renameTo(newSourceGroup);
          sourceGroup = newSourceGroup;

          File[] sourceFiles = sourceGroup.listFiles();
          File sourceFile = sourceFiles[new Random().nextInt(sourceFiles.length)];
          renameCopy(sourceFile, destinationDirectory);
        }

      }
    }
    catch (Exception exception)
    {
      JOptionPane.showMessageDialog(null, exception.getMessage(), exception.getClass().toString(), JOptionPane.ERROR_MESSAGE);
      throw exception;
    }
  }

  private static void renameCopy(File sourceFile, File destinationDirectory) throws IOException
  {
    String sourceFileName = sourceFile.getName();
    File newSourceFile = new File(sourceFile.getParent() + "\\" + System.currentTimeMillis() + sourceFileName.substring(sourceFileName.lastIndexOf('.')));
    log("Rename", sourceFile.getAbsolutePath(), newSourceFile.getAbsolutePath());
    sourceFile.renameTo(newSourceFile);
    sourceFile = newSourceFile;
    String destinationFileName = destinationDirectory.getAbsolutePath() + "\\" + sourceFile.getName();
    log("Copy", sourceFile.getAbsolutePath(), destinationFileName);
    Files.copy(sourceFile.toPath(), Paths.get(destinationFileName));
  }

  private static File getDirectory(String key)
  {
    String[] directoryNames = PROPERTIES.getProperty(key).split(",");
    return new File(directoryNames[new Random().nextInt(directoryNames.length)]);
  }

  private static void log(String... logs)
  {
    for (String log : logs)
    {
      System.out.println(new Date() + " " + log);
    }
  }

}
