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


/**
 * Wait for user input, intended as a method of pausing for interactive testing
 *
 * @goal wait
 */
public class WaitMojo extends AbstractMojo {

  /**
   * The prompt message displayed before waiting
   * @parameter default-value="Press ENTER key to continue..."
   */
  private String promptMessage;

  /**
   * The finished waiting message
   * @parameter default-value="Thanks, done waiting"
   */
  private String completionMessage;

  @Override
  public void execute() throws MojoExecutionException, MojoFailureException {
    System.out.println(promptMessage);
    wait(System.in);
    System.out.println(completionMessage);
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
