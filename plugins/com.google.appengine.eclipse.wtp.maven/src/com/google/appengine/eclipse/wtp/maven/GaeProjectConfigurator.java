/*******************************************************************************
 * Copyright 2013 Google Inc. All Rights Reserved.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *******************************************************************************/
package com.google.appengine.eclipse.wtp.maven;

import java.util.List;

import org.apache.maven.model.Model;
import org.apache.maven.model.Plugin;
import org.apache.maven.plugin.MojoExecution;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.m2e.core.lifecyclemapping.model.IPluginExecutionMetadata;
import org.eclipse.m2e.core.project.IMavenProjectFacade;
import org.eclipse.m2e.core.project.configurator.AbstractBuildParticipant;
import org.eclipse.m2e.core.project.configurator.ProjectConfigurationRequest;
import org.eclipse.m2e.wtp.WTPProjectConfigurator;
import org.eclipse.wst.common.project.facet.core.IFacetedProject;
import org.eclipse.wst.common.project.facet.core.ProjectFacetsManager;

/**
 * A {@link WTPProjectConfigurator} that adds GAE and JPA facets to GAE projects.
 */
@SuppressWarnings("restriction") // WTPProjectConfigurator
public class GaeProjectConfigurator extends WTPProjectConfigurator {

  @Override
  public void configure(ProjectConfigurationRequest request, IProgressMonitor monitor)
      throws CoreException {
    AppEngineMavenPlugin.logInfo("GaeProjectConfigurator.configure invoked.");
    Model pom = request.getMavenProject().getModel();
    if (isGaeProject(pom)) {
      IFacetedProject facetedProject = ProjectFacetsManager.create(request.getProject());
      new GaeFacetManager().addGaeFacet(pom, facetedProject, monitor);
      new JpaFacetManager().addJpaFacet(facetedProject, monitor);
    }
  }

  @Override
  public AbstractBuildParticipant getBuildParticipant(
      IMavenProjectFacade projectFacade, MojoExecution execution,
      IPluginExecutionMetadata executionMetadata) {
    AppEngineMavenPlugin.logInfo("GaeProjectConfigurator.getBuildParticipant invoked");
    return super.getBuildParticipant(projectFacade, execution, executionMetadata);
  }
  
  private static boolean isGaeProject(Model pom) {
    List<Plugin> plugins = pom.getBuild().getPlugins();
    for (Plugin plugin : plugins) {
      if (Constants.APPENGINE_GROUP_ID.equals(plugin.getGroupId())
          && Constants.APPENGINE_MAVEN_PLUGIN_ARTIFACT_ID.equals(plugin.getArtifactId())) {
        return true;
      }
    }
    return false;
  }

}