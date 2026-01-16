package com.samos.core.exception.basic;

import com.samos.core.exception.FunctionalError;

/**
 * La classe {@code DataAccessUnauthorizedError} est une forme de {@link RuntimeException}
 * qui représente une erreur fonctionnelle indiquant que l'appelant doit s'authentifier
 * pour accès à la donnée.
 * <p>
 *
 * @see FunctionalError
 */
public class DataAccessUnauthorizedError extends FunctionalError { // NOSONAR

	private static final long serialVersionUID = 7459658611231597535L;

	/**
	 * Construit une nouvelle erreur fonctionnelle indiquant que l'appelant doit
	 * s'authentifier pour accès à la donnée.
	 */
	public DataAccessUnauthorizedError() {
		this(null);
	}

	/**
	 * Construit une nouvelle erreur fonctionnelle indiquant que l'appelant doit
	 * s'authentifier pour accès à la donnée, en précisant un message d'erreur et des
	 * paramètres pour l'erreur (faculatif).<br>
	 * Les paramètres permettent d'isoler le contenu statique du message du contenu
	 * variable, permettant ainsi d'identifier plus facilement un motif d'erreur.<br>
	 * <b>Tout message qui comporte des données variables doit utiliser les paramètres
	 * pour transmettre ces données variables.</b>
	 *
	 * @param message le message d'erreur. <br>
	 *     Le message peut comporter des paramètres qui doivent être formalisés par des
	 *     accolades "{}".
	 * @param parameters (faculatif) les paramètres du message d'erreur. <br>
	 *     Les paramètres doivent être transmis dans l'ordre dans lequel ils apparaissent
	 *     dans le message.
	 * 
	 *     <p>
	 *     Exemple:
	 * 
	 *     <pre>
	 * <code class='java'>
	 * // Crée une erreur fonctionnelle avec des paramètres
	 * throw new DataAccessUnauthorizedError("L'appelant doit s'authentifier pour accéder aux données du client {}.", "007");
	 *</code>
	 *     </pre>
	 */
	public DataAccessUnauthorizedError(String message, Object... parameters) {
		this(null, message, parameters);
	}

	/**
	 * Identique à {@link #DataAccessUnauthorizedError} mais permet de
	 * préciser la root cause de l'erreur.
	 * 
	 * @param cause la root cause de l'erreur.
	 */
	public DataAccessUnauthorizedError(Throwable cause, String message,
			Object... parameters) {
		super(BasicErrorCode.UNAUTHORIZED, cause, message, parameters);
	}

	@Override
	public String toString() {
		return "DataAccessUnauthorizedError [" + //
				"code=" + getCode().name() + //
				(getMessageTemplate() != null ? ", message=" + getMessageTemplate() : "")
				+ //
				(getParameters() != null && !getParameters().isEmpty()
						? ", parameters=" + getParameters()
						: "")
				+ "]";
	}
}
