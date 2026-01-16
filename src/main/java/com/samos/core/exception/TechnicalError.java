package com.samos.core.exception;

import lombok.Getter;

/**
 * La classe {@code TechnicalError} représente une erreur technique.
 * <p>
 * Une erreur technique est une erreur dont le comportement n'est pas liée au métier.
 *
 */
@Getter
public class TechnicalError extends ErrorWithParameters {

	private static final long serialVersionUID = 6000076138379017453L;

	/**
	 * L'interface fonctionnelle {@code ErrorCode} permet de caractériser une erreur
	 * technique.
	 * <p>
	 * 
	 * Exemples:
	 * 
	 * <pre>
	 * <code class='java'>
	 * // Crée une erreur technique avec un code d'erreur (en utilisant une fonction lambda)
	 * throw new TechnicalError(() -> "DATABASE_UNREACHABLE");
	 * </code>
	 * </pre>
	 * 
	 * @see ErrorCode
	 */
	public interface ErrorCode {
		String name();
	}

	/**
	 * La classe {@code TechnicalErrorType} permet de caractériser une erreur technique.
	 * <p>
	 * Une erreur technique peut être :
	 * <ul>
	 * <li><b>fatale</b>: rejouer le traitement qui entrainé l'erreur n'a aucune chance
	 * d'aboutir.</li>
	 * <li><b>rejouable</b>: rejouer le traitement peut lui permettre d'aboutir.</li>
	 * </ul>
	 */
	public enum TechnicalErrorType {
		FATAL, RETRIABLE
	}

	private final ErrorCode code; // NOSONAR (Make "code" transient or serializable. =>
	// Si on fait Ã§a, le code ne sort plus dans le
	// toString)

	final TechnicalErrorType type;

	/**
	 * Construit une nouvelle erreur technique fatale
	 * ({@link TechnicalError.TechnicalErrorType}) avec un message d'erreur et des
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
	 * // Crée une erreur technique avec des paramètres
	 * throw new TechnicalError("Impossible de lire le fichier {}.",
	 *	"/tmp/cac40_tips.json");
	 *</code>
	 *     </pre>
	 */
	public TechnicalError(String message, Object... parameters) {
		this(null, null, TechnicalErrorType.FATAL, message, parameters);
	}

	/**
	 * Construit une nouvelle erreur technique fatale
	 * ({@link TechnicalError.TechnicalErrorType}) avec une root cause.<br>
	 *
	 * @param cause la root cause de l'erreur.
	 * 
	 *     <p>
	 *     Exemple:
	 * 
	 *     <pre>
	 * <code class='java'>
	 * 
	 * final List<String> lines;
	 * try {
	 *     lines = Files.readAllLines(Paths.get("monSuperFichier.csv")); // java.nio.file.Files.readAllLines(Path path) m'impose de gérer le cas oÃ¹ une IOException surviendrait
	 * } // Mon bloc try/catch ne doit porter QUE sur la ligne qui m'impose de gérer l'exception, pas le pavé de 10 lignes avant et après (sinon, on ne sait pas quelle est la ligne qui est concernée + on complexifie la lecture du code)
	 * catch (IOException e) { // Je capture EXPLICITEMENT la ou les exceptions qui me sont demandées, pas de raccourci du genre "catch (Exception e)" qui masquerait ce qui est réellement capturé
	 *     throw new TechnicalError(e); // Je re-throw en TechnicalError, avec l'IOException d'origine en tant que root cause, en complétant avec un message [+paramètres] uniquement si Ã§a apporte un plus pour l'exploitation (=une info qui ne serait pas présente dans la stacktrace et qui serait importante pour l'analyse).
	 *     // Trace dans les logs: TechnicalError [type=FATAL] + stack erreur avec root cause
	 * }
	 *</code>
	 *     </pre>
	 */
	public TechnicalError(Throwable cause) {
		this(null, cause, TechnicalErrorType.FATAL, null, (Object[]) null);
	}

	/**
	 * Identique à {@link #TechnicalError} mais permet de préciser la
	 * root cause de l'erreur.
	 * 
	 * @param cause la root cause de l'erreur.
	 */
	public TechnicalError(Throwable cause, String message, Object... parameters) {
		this(null, cause, TechnicalErrorType.FATAL, message, parameters);
	}

	/**
	 * Identique à {@link #TechnicalError} mais permet de préciser le
	 * type de l'erreur ({@link TechnicalError.TechnicalErrorType}).
	 * 
	 * @param type le type de l'erreur.
	 */
	public TechnicalError(TechnicalErrorType type, String message, Object... parameters) {
		this(null, null, type, message, parameters);
	}

	/**
	 * Identique à {@link #TechnicalError} mais permet de préciser la
	 * root cause de l'erreur et le type de l'erreur.
	 * ({@link TechnicalError.TechnicalErrorType}).
	 * 
	 * @param cause la root cause de l'erreur.
	 * @param type le type de l'erreur.
	 */
	public TechnicalError(Throwable cause, TechnicalErrorType type, String message,
			Object... parameters) {
		this(null, cause, type, message, parameters);
	}

	/**
	 * Identique à {@link #TechnicalError} mais permet de préciser la
	 * root cause de l'erreur, le type de l'erreur et le code de l'erreur
	 * ({@link TechnicalError.TechnicalErrorType}).
	 * 
	 * @param code le code de l'erreur. Le code est enregistré pour pouvoir être récupéré
	 *     ultérieurement via la méthode {@link #getCode()}.
	 * @param cause la root cause de l'erreur.
	 * @param type le type de l'erreur.
	 */
	public TechnicalError(ErrorCode code, Throwable cause, TechnicalErrorType type,
			String message, Object... parameters) {
		super(cause, message, parameters);
		this.code = code;
		this.type = type;
	}

	@Override
	public String toString() {
		return "TechnicalError [" + //
				(code != null ? "code=" + code.name() + ", " : "") + //
				(getMessageTemplate() != null ? "message=" + getMessageTemplate() + ", "
						: "")
				+ //
				(getParameters() != null && !getParameters().isEmpty()
						? "parameters=" + getParameters() + ", "
						: "")
				+ //
				"type=" + type + //
				"]";
	}

}