/**
 * Copyright (c) 2010 RedEngine Ltd, http://www.redengine.co.nz. All rights reserved.
 *
 * This program is licensed to you under the Apache License Version 2.0,
 * and you may not use this file except in compliance with the Apache License Version 2.0.
 * You may obtain a copy of the Apache License Version 2.0 at http://www.apache.org/licenses/LICENSE-2.0.
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the Apache License Version 2.0 is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the Apache License Version 2.0 for the specific language governing permissions and limitations there under.
 */
package net.stickycode.plugin.wait;

import java.io.IOException;
import java.io.InputStream;

import org.apache.maven.execution.MavenSession;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.project.MavenProject;

/**
 * Wait for user input, intended as a method of pausing for interactive testing
 *
 */
@Mojo(threadSafe = true, name = "wait", requiresDirectInvocation = true)
public class WaitMojo
    extends AbstractMojo {

  private final class LetItFinish
      implements Runnable {

    private Thread thread;

    private boolean waiting = true;

    public LetItFinish(Thread thread) {
      this.thread = thread;
    }

    @Override
    public void run() {
      if (waiting) {
        thread.interrupt();

        try {
          for (int i = 0; waiting && i < shutdownLatency; i += 1) {
            // give it a little bit of time to write to the console
            Thread.sleep(1000);
            // we don't care what the result is only that there is one
            waiting = session.getResult().getBuildSummary(project) == null;
          }
        }
        catch (InterruptedException e) {
        }
      }
    }

    public void userIsFinished() {
      waiting = false;
    }
  }

  /**
   * The prompt message displayed before waiting
   * 
   */
  @Parameter(defaultValue = "Press ENTER key to continue...")
  private String promptMessage;

  /**
   * The finished waiting message
   * 
   */
  @Parameter(defaultValue = "Thanks, done waiting")
  private String completionMessage;

  /**
   * By default don't wait, just wait when the property 'wait' is set
   */
  @Parameter(defaultValue = "false", property = "wait")
  private Boolean wait = false;

  /**
   * Time to wait for the build to finish after releasing the pause
   */
  @Parameter(defaultValue = "60")
  private int shutdownLatency = 60;

  @Parameter(defaultValue = "${project}")
  private MavenProject project;

  @Parameter(defaultValue = "${session}")
  private MavenSession session;

  @Override
  public void execute() throws MojoExecutionException, MojoFailureException {
    if (wait) {
      System.out.println(promptMessage);
      wait(System.in);
      System.out.println(completionMessage);
    }
    else
      getLog().info("Not waiting, -Dwait=true");
  }

  void wait(InputStream in) throws MojoExecutionException {
    LetItFinish cleanup = new LetItFinish(Thread.currentThread());
    Runtime.getRuntime().addShutdownHook(new Thread(cleanup));
    try {
      while (in.available() == 0)
        Thread.sleep(1000);

      cleanup.userIsFinished();
    }
    catch (IOException e) {
      throw new MojoExecutionException("Failed to read user input", e);
    }
    catch (InterruptedException e) {
    }
  }

}
