package com.samos.core.exception;

import java.util.Arrays;
import java.util.List;

import org.slf4j.helpers.MessageFormatter;

import lombok.Getter;

/**
 * Extension de la classe {@link RuntimeException} permettant de gérer des paramètres
 * d'erreurs.
 */
@Getter
public abstract class ErrorWithParameters extends RuntimeException {

	private static final long serialVersionUID = -9029239721066151134L;

	private final String messageTemplate;
	private final List<Object> parameters; // NOSONAR: ignore Fields in a "Serializable"
											// class should either be transient or
											// serializable

	/**
	 * Constructeur.
	 *
	 * @param cause l'erreur d'origine
	 * @param messageTemplate le message d'erreur
	 * @param parameters les paramètres du message d'erreur
	 */
	protected ErrorWithParameters(Throwable cause, String messageTemplate,
			Object... parameters) {
		// L'appel au constructeur parent doit être fait en premier, ce qui nous amène à
		// centraliser le contrôle du message templatisé dans la fonction
		// formatMessageWithParameters plutôt que de tout faire directement dans cette fonction
		super(formatMessageWithParameters(messageTemplate, parameters), cause);

		this.messageTemplate = messageTemplate;

		this.parameters = parameters != null && parameters.length > 0
				? Arrays.asList(parameters)
				: null;
	}

	/**
	 * Remplace les "{}" présentes dans le message par les paramètres, en respectant
	 * l'ordre des paramètres.
	 * 
	 * @param messageTemplate le message
	 * @param parameters les paramètres
	 * @return le message détemplatisé
	 */
	private static String formatMessageWithParameters(String messageTemplate,
			Object... parameters) {

		final String formatedMessage;

		// Pas de message = pas de détemplatisation
		if (messageTemplate == null) {
			formatedMessage = null;
		}
		// Pas de paramètres = pas de détemplatisation
		else if (parameters == null) {
			formatedMessage = messageTemplate;
		}
		else {
			// On utilise le formateur de SLF4J, pour ne pas réinventer la roue
			formatedMessage = MessageFormatter.arrayFormat(messageTemplate, parameters)
					.getMessage();
		}

		return formatedMessage;
	}

}
