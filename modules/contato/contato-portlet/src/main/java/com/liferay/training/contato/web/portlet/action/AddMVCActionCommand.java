package com.liferay.training.contato.web.portlet.action;

import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.portlet.bridges.mvc.MVCActionCommand;
import com.liferay.portal.kernel.service.ServiceContext;
import com.liferay.portal.kernel.service.ServiceContextFactory;
import com.liferay.portal.kernel.servlet.SessionErrors;
import com.liferay.portal.kernel.servlet.SessionMessages;
import com.liferay.portal.kernel.theme.ThemeDisplay;
import com.liferay.portal.kernel.util.WebKeys;
import com.liferay.training.contato.model.Contato;
import com.liferay.training.contato.service.ContatoService;
import com.liferay.training.contato.web.constants.CommandNames;
import com.liferay.training.contato.web.constants.ContatoPortletKeys;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;

@Component(
        immediate = true,
        property = {
            "javax.portlet.name=" + ContatoPortletKeys.CONTATO,
                "mvc.command.name=" + CommandNames.HANDLE_FORM
        },
        service = MVCActionCommand.class
)
public class AddMVCActionCommand implements MVCActionCommand {

    @Override
    public boolean processAction(ActionRequest actionRequest, ActionResponse actionResponse) {

        String nome = actionRequest.getParameter("nome");
        String telefone = actionRequest.getParameter("telefone");
        String email = actionRequest.getParameter("email");
        Integer idade = Integer.valueOf(actionRequest.getParameter("idade"));

        try {
            ServiceContext serviceContext = ServiceContextFactory.getInstance(
                    Contato.class.getName(), actionRequest);

            ThemeDisplay themeDisplay = (ThemeDisplay) actionRequest.getAttribute(WebKeys.THEME_DISPLAY);
            _contatoService.addContato(themeDisplay.getScopeGroupId(), nome, telefone, email, idade, serviceContext);

            SessionMessages.add(actionRequest, "contactAdded");

            return true;
        } catch (PortalException pe) {
            SessionErrors.add(actionRequest, "serviceErrorDetails", pe);

            actionResponse.setRenderParameter(
                    "mvcRenderCommandName", CommandNames.CADASTRO);
        }

        return false;
    }

    @Reference
    protected ContatoService _contatoService;

}
