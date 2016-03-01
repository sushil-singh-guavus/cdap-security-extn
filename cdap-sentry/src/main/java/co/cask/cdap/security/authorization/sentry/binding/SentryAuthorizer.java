/*
 *
 * Copyright © 2016 Cask Data, Inc.
 *  
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *  
 * http://www.apache.org/licenses/LICENSE-2.0
 *  
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */

package co.cask.cdap.security.authorization.sentry.binding;

import co.cask.cdap.common.UnauthorizedException;
import co.cask.cdap.common.conf.CConfiguration;
import co.cask.cdap.proto.id.EntityId;
import co.cask.cdap.proto.security.Action;
import co.cask.cdap.proto.security.Principal;
import co.cask.cdap.security.authorization.Authorizer;
import co.cask.cdap.security.authorization.sentry.binding.conf.AuthConf;
import com.google.common.base.Preconditions;
import com.google.inject.Inject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Set;

/**
 * Created by rsinha on 2/29/16.
 */
public class SentryAuthorizer implements Authorizer {

  private static final Logger LOG = LoggerFactory.getLogger(SentryAuthorizer.class);

  AuthBinding binding;
  AuthConf authConf;

  String sentrySite = null;
//  List<KafkaPrincipal> super_users = null;

  @Inject
  public SentryAuthorizer(CConfiguration cConf) {
    final String sentrySiteUrlConfig = cConf.get(AuthConf.SENTRY_SITE_URL);

    Preconditions.checkNotNull(sentrySiteUrlConfig, "sentry-site.xml path is null in cdap-site.xml");

    this.sentrySite = sentrySiteUrlConfig;
    LOG.info("Configuring Sentry Authorizer: " + sentrySite);
    final AuthBindingSingleton instance = AuthBindingSingleton.getInstance();
    instance.configure(sentrySite);
    this.binding = instance.getAuthBinding();
    this.authConf = instance.getAuthConf();
  }

  @Override
  public void grant(EntityId entityId, Principal principal, Set<Action> set) {
    throw new UnsupportedOperationException("Please use Sentry CLI to perform this action.");
  }

  @Override
  public void grant(EntityId entityId, Principal principal) {
    throw new UnsupportedOperationException("Please use Sentry CLI to perform this action.");
  }

  @Override
  public void revoke(EntityId entityId, Principal principal, Set<Action> set) {
    throw new UnsupportedOperationException("Please use Sentry CLI to perform this action.");
  }

  @Override
  public void revoke(EntityId entityId, Principal principal) {
    throw new UnsupportedOperationException("Please use Sentry CLI to perform this action.");
  }

  @Override
  public void revoke(EntityId entityId) {
    throw new UnsupportedOperationException("Please use Sentry CLI to perform this action.");
  }

  @Override
  public void enforce(EntityId entityId, Principal principal, Action action) throws UnauthorizedException {
    if (!binding.authorize(entityId, principal, action)) {
      throw new UnauthorizedException(String.format("User {} is unauthorized to perform action {} on entitiy {}",
                                                    principal.getName(), action.name(), entityId.toString()));
    }
  }
}
