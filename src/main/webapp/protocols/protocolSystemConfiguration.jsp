<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-html" prefix="html" %>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean" prefix="bean" %>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-logic" prefix="logic" %>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<style>
select {
	max-width: 700px;
}
</style>

<h2>
	<bean:message key="label.protocolSystem.configure" bundle="PROTOCOLS_RESOURCES" />

	<small class="pull-right">
		<html:link action="/protocolsConfiguration.do?method=reloadCountries">
			<bean:message key="label.countries.reload" bundle="PROTOCOLS_RESOURCES"/>
		</html:link>
	</small>
</h2>

<fr:form action="/protocolsConfiguration.do?method=protocolSystemConfiguration">

<div class="col-lg-12">
	<div class="row">
		<div class="infobox text-center">
			<strong><bean:message key="label.organizationalModel.define" bundle="PROTOCOLS_RESOURCES"/></strong>
		</div>
	</div>
	<div class="row">
		<div class="col-sm-6">
			<div class="infobox text-center">
				<strong><bean:message key="label.organizationalModel.internal" bundle="PROTOCOLS_RESOURCES"/></strong>
			</div>

			<fr:edit name="configurationBean" id="internal">
				<fr:schema type="module.protocols.dto.ProtocolSystemConfigurationBean" bundle="PROTOCOLS_RESOURCES">
					<fr:slot name="internalOrganizationalModel" layout="menu-select" key="label.organizationalModel.internal">
						<fr:property name="providerClass" value="module.organization.presentationTier.renderers.providers.OrganizationalModelProvider"/>
						<fr:property name="format" value="\${name.content}" />
					</fr:slot>
				</fr:schema>
				<fr:layout name="tabular">
					<fr:property name="classes" value="form listInsideClear" />
					<fr:property name="columnClasses" value="width100px,,tderror" />
				</fr:layout>
			</fr:edit>
		</div>
		<div class="col-sm-6">
			<div class="infobox text-center">
				<strong><bean:message key="label.organizationalModel.external" bundle="PROTOCOLS_RESOURCES"/></strong>
			</div>

			<fr:edit name="configurationBean" id="external">
				<fr:schema type="module.protocols.dto.ProtocolSystemConfigurationBean" bundle="PROTOCOLS_RESOURCES">
					<fr:slot name="externalOrganizationalModel" layout="menu-select" key="label.organizationalModel.external">
						<fr:property name="providerClass" value="module.organization.presentationTier.renderers.providers.OrganizationalModelProvider"/>
						<fr:property name="format" value="\${name.content}" />
					</fr:slot>
				</fr:schema>
				<fr:layout name="tabular">
					<fr:property name="classes" value="form listInsideClear" />
					<fr:property name="columnClasses" value="width100px,,tderror" />
				</fr:layout>
			</fr:edit>
		</div>
	</div>
</div>

<div align="center">
	<input type="submit" value="<bean:message key="submit.submit" bundle="PROTOCOLS_RESOURCES"/>" class="btn btn-default" />
</div>

</fr:form>
<hr />
<div class="row">
	<div class="col-sm-6">
		<h3 class="text-center">${fr:message('resources.ProtocolsResources', 'label.protocolSystem.configureAuthorizationGroups')}</h3>
		<table class="table table-striped">
			<thead class="text-center">
				<th>${fr:message('resources.ProtocolsResources', 'label.protocolSystem.authorizationGroups')}</th>
				<th>Operações</th>
			</thead>
			<tbody>
				<c:forEach var="group" items="${authorizationGroups}">
				<tr>
					<td>${group.writerGroup}</td>
					<td>
						<a href="${pageContext.request.contextPath}/protocolsConfiguration.do?method=configureAuthorizationGroup&group=${group.externalId}">
							${fr:message('resources.ProtocolsResources', 'link.configure')}
						</a>
						<c:if test="${group.writerProtocolsSet.size() == 0}">
							- <a href="${pageContext.request.contextPath}/protocolsConfiguration.do?method=removeAuthorizationGroup&OID=${group.externalId}">
								${fr:message('resources.ProtocolsResources', 'link.remove')}
							</a>
						</c:if>
					</td>
				</tr>
				</c:forEach>
			</tbody>
		</table>
	</div>
	<div class="col-sm-6 text-center">
		<h3>${fr:message('resources.ProtocolsResources', 'label.protocolSystem.addAuthorizationGroup')}</h3>
		<form action="${pageContext.request.contextPath}/protocolsConfiguration.do?method=createNewAuthorizationGroup" class="form-inline" method="POST">
			<div class="form-group">
				<input class="form-control" type="text" name="expression" required placeholder="Expression" />
			</div>
			<input type="submit" value="${fr:message('resources.ProtocolsResources', 'submit.submit')}" class="btn btn-default" />
		</form>
	</div>
</div>
