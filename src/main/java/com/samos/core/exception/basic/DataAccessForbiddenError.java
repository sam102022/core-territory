package com.samos.core.exception.basic;

import com.samos.core.exception.FunctionalError;

/**
 * La classe {@code DataAccessForbiddenError} est une forme de {@link RuntimeException}
 * qui représente une erreur fonctionnelle indiquant que l'accès à la donnée est interdit
 * pour cet appelant.
 * <p>
 *
 * @see FunctionalError
 */
public class DataAccessForbiddenError extends FunctionalError { // NOSONAR

	private static final long serialVersionUID = -2740387355089992512L;

	/**
	 * Construit une nouvelle erreur fonctionnelle indiquant que l'accès à la donnée est
	 * interdit pour cet appelant.
	 */
	public DataAccessForbiddenError() {
		this(null);
	}

	/**
	 * Construit une nouvelle erreur fonctionnelle indiquant que l'accès à la donnée est
	 * interdit pour cet appelant, en précisant un message d'erreur et des paramètres pour
	 * l'erreur (falcultatif).<br>
	 * Les paramètres permettent d'isoler le contenu statique du message du contenu
	 * variable, permettant ainsi d'identifier plus facilement un motif d'erreur.<br>
	 * <b>Tout message qui comporte des données variables doit utiliser les paramètres
	 * pour transmettre ces données variables.</b>
	 *
	 * @param message le message d'erreur. <br>
	 *     Le message peut comporter des paramètres qui doivent être formalisés par des
	 *     accolades "{}".
	 * @param parameters (falcultatif) les paramètres du message d'erreur. <br>
	 *     Les paramètres doivent être transmis dans l'ordre dans lequel ils apparaissent
	 *     dans le message.
	 * 
	 *     <p>
	 *     Exemple:
	 * 
	 *     <pre>
	 * <code class='java'>
	 * // Crée une erreur fonctionnelle avec des paramètres
	 * throw new DataAccessForbiddenError("L'appelant n'a pas le droit d'accéder aux données du client {}.", "007");
	 *</code>
	 *     </pre>
	 */
	public DataAccessForbiddenError(String message, Object... parameters) {
		this(null, message, parameters);
	}

	/**
	 * Identique à {@link #DataAccessForbiddenError} mais permet de
	 * préciser la root cause de l'erreur.
	 * 
	 * @param cause la root cause de l'erreur.
	 */
	public DataAccessForbiddenError(Throwable cause, String message,
			Object... parameters) {
		super(BasicErrorCode.FORBIDDEN, cause, message, parameters);
	}

	@Override
	public String toString() {
		return "DataAccessForbiddenError [" + //
				"code=" + getCode().name() + //
				(getMessageTemplate() != null ? ", message=" + getMessageTemplate() : "")
				+ //
				(getParameters() != null && !getParameters().isEmpty()
						? ", parameters=" + getParameters()
						: "")
				+ "]";
	}
}
