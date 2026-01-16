package com.samos.core.exception;

import lombok.Getter;

/**
 * La classe {@code FunctionalError} ainsi que toutes les classes qui en héritent sont une
 * forme de {@link RuntimeException} qui représentent une erreur fonctionnelle.
 * <p>
 * Une erreur fonctionnelle est une erreur dont le comportement est prévu et lié au
 * métier.<br>
 * Elle est caractérisée au minimum par un {@link ErrorCode}.
 *
 */
@Getter
public class FunctionalError extends ErrorWithParameters {

	private static final long serialVersionUID = -7228721566523367783L;

	/**
	 * L'interface fonctionnelle {@code ErrorCode} permet de caractériser une erreur
	 * fonctionnelle.
	 * <p>
	 * Pour les erreurs fonctionnelles basiques, vous pouvez directement utiliser l'une
	 * des implémentations suivantes:
	 * <ul>
	 * <li>{@link com.samos.api.territory.exception.basic.InvalidFormatError}</li>
	 * <li>{@link com.samos.api.territory.exception.basic.DataNotFoundError}</li>
	 * <li>{@link com.samos.api.territory.exception.basic.DataAccessForbiddenError}</li>
	 * <li>{@link com.samos.api.territory.exception.basic.DataAccessUnauthorizedError}</li>
	 * </ul>
	 * 
	 * Exemples:
	 * 
	 * <pre>
	 * <code class='java'>
	 * // Crée une erreur fonctionnelle avec un code d'erreur basique
	 * throw new FunctionalError(BasicErrorCode.UNAUTHORIZED);
	 * 
	 * // Equivalent de l'exemple précédent pour créer une erreur fonctionnelle avec le code d'erreur basique UNAUTHORIZED
	 * throw new DataAccessUnauthorizedError();
	 * 
	 * // Crée une erreur fonctionnelle avec un code d'erreur spécifique (en utilisant une fonction lambda)
	 * throw new FunctionalError(() -> "BANK_ACCOUNT_BANNED");
	 * </code>
	 * </pre>
	 * 
	 * @see ErrorCode
	 */
	public interface ErrorCode {
		String name();
	}

	/**
	 * La classe {@code BasicErrorCode} propose une liste de codes basiques permettant de
	 * caractériser une erreur fonctionnelle.
	 * <p>
	 * @see FunctionalError.ErrorCode
	 */
	public enum BasicErrorCode implements ErrorCode {
		UNAUTHORIZED, //
		FORBIDDEN, //
		NOT_FOUND, //
		INVALID_FORMAT
	}

	private final ErrorCode code; // NOSONAR (Make "code" transient or serializable. =>
									// Si on fait Ã§a, le code ne sort plus dans le
									// toString)

	/**
	 * Construit une nouvelle erreur fonctionnelle avec le code d'erreur précisé.
	 *
	 * @param code le code de l'erreur, de type {@link ErrorCode}.
	 *     <p>
	 *     Exemples:
	 * 
	 *     <pre>
	 * <code class='java'>
	 * // Crée une erreur fonctionnelle avec un code d'erreur basique
	 * throw new FunctionalError(BasicErrorCode.UNAUTHORIZED);
	 * 
	 * // Crée une erreur fonctionnelle avec un code d'erreur spécifique
	 * throw new FunctionalError(() -> "BANK_ACCOUNT_BANNED");
	 * </code>
	 *     </pre>
	 */
	public FunctionalError(ErrorCode code) {
		this(code, null);
	}

	/**
	 * Construit une nouvelle erreur fonctionnelle avec le code d'erreur précisé, un
	 * message d'erreur et des paramètres pour l'erreur (faculatif).<br>
	 * Les paramètres permettent d'isoler le contenu statique du message du contenu
	 * variable, permettant ainsi d'identifier plus facilement un motif d'erreur.<br>
	 * <b>Tout message qui comporte des données variables doit utiliser les paramètres
	 * pour transmettre ces données variables.</b>
	 *
	 * @param code le code de l'erreur. Le code est enregistré pour pouvoir être récupéré
	 *     ultérieurement via la méthode {@link #getCode()}.
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
	 * throw new FunctionalError(() -> "BANK_ACCOUNT_BANNED",
	 *	"L'IBAN {} est banni (commande {}).", "FR7630006000011234567890189", "123456");
	 *</code>
	 *     </pre>
	 */
	public FunctionalError(ErrorCode code, String message, Object... parameters) {
		this(code, null, message, parameters);
	}

	/**
	 * Identique à {@link #FunctionalError} mais permet de
	 * préciser la root cause de l'erreur.
	 * 
	 * @param cause la root cause de l'erreur.
	 */
	public FunctionalError(ErrorCode code, Throwable cause, String message,
			Object... parameters) {
		super(cause, message, parameters);
		this.code = code;
	}

	@Override
	public String toString() {
		return "FunctionalError [" + //
				"code=" + code.name() + //
				(getMessageTemplate() != null ? ", message=" + getMessageTemplate() : "")
				+ //
				(getParameters() != null && !getParameters().isEmpty()
						? ", parameters=" + getParameters()
						: "")
				+ "]";
	}

}
