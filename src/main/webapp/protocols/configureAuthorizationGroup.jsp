<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-html" prefix="html" %>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean" prefix="bean" %>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-logic" prefix="logic" %>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>


<div class="row">

<h2>
	${fr:message('resources.ProtocolsResources', 'label.protocolSystem.configureAuthorizationGroup')}
	<small>${group.writerGroup}</small>
</h2>

<div class="col-sm-6">
	<h4 class="text-center">Reader Groups</h4>
	<table class="table table-striped">
	<thead class="text-center">
		<th>${fr:message('resources.ProtocolsResources', 'label.protocolSystem.authorizationGroups')}</th>
		<th></th>
	</thead>
	<tbody>
		<c:forEach var="reader" items="${group.authorizedReaderGroupsSet}">
		<tr>
			<td>${reader.presentationName}</td>
			<td>
				<a href="${pageContext.request.contextPath}/protocolsConfiguration.do?method=revoke&group=${group.externalId}&reader=${reader.externalId}">
					${fr:message('resources.ProtocolsResources', 'link.remove')}
				</a>
			</td>
		</tr>
		</c:forEach>
	</tbody>
</table>
</div>

<div class="col-sm-6 text-center">
	<h4>${fr:message('resources.ProtocolsResources', 'label.protocolSystem.addAuthorizationGroup')}</h4>
	<form action="${pageContext.request.contextPath}/protocolsConfiguration.do?method=addReaderToGroup&group=${group.externalId}" class="form-inline" method="POST">
		<div class="form-group">
			<input class="form-control" type="text" name="expression" required placeholder="Expression" />
		</div>
		<input type="submit" value="${fr:message('resources.ProtocolsResources', 'submit.submit')}" class="btn btn-default" />
	</form>
</div>

</div>