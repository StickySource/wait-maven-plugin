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

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;


/**
 * @goal pause
 */
public class PauseMojo extends AbstractMojo {

  /**
   * @parameter expression="${timeout}" alias="timeout"
   */
  private long timeout;

  @Override
  public void execute() throws MojoExecutionException, MojoFailureException {
    System.out.println("Sleeping for " + timeout + "ms");
    try {
      Thread.sleep(timeout);
    }
    catch (InterruptedException e) {
      throw new MojoExecutionException("Sleep was interupted", e);
    }

  }

}
