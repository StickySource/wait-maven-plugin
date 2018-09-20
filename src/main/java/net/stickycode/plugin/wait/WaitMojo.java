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

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;

/**
 * Wait for user input, intended as a method of pausing for interactive testing
 *
 */
@Mojo(threadSafe = true, name = "wait", requiresDirectInvocation = true)
public class WaitMojo
    extends AbstractMojo {

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

  @Override
  public void execute() throws MojoExecutionException, MojoFailureException {
    if (wait) {
      System.out.println(promptMessage);
      wait(System.in);
      System.out.println(completionMessage);
    }
    else
      getLog().info("Not waiting for anything");
  }

  void wait(InputStream in) throws MojoExecutionException {
    try {
      in.read();
    }
    catch (IOException e) {
      throw new MojoExecutionException("Failed to read user input", e);
    }
  }

}
