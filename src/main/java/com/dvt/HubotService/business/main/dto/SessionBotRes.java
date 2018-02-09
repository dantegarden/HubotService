package com.dvt.HubotService.business.main.dto;

import java.util.List;

public class SessionBotRes {
	/**意图候选项**/
	private List<SessionBotResIntentCandidate> intent_candidates;
	/**词法分析结果**/
	private List<SessionBotResLexicalAnalysis> lexical_analysis;
	/**情感分析结果**/
	private SessionBotResSentimentAnalysis sentiment_analysis;
	
	public SessionBotRes(List<SessionBotResIntentCandidate> intent_candidates,
			List<SessionBotResLexicalAnalysis> lexical_analysis,
			SessionBotResSentimentAnalysis sentiment_analysis) {
		super();
		this.intent_candidates = intent_candidates;
		this.lexical_analysis = lexical_analysis;
		this.sentiment_analysis = sentiment_analysis;
	}
	public List<SessionBotResIntentCandidate> getIntent_candidates() {
		return intent_candidates;
	}
	public void setIntent_candidates(
			List<SessionBotResIntentCandidate> intent_candidates) {
		this.intent_candidates = intent_candidates;
	}
	public List<SessionBotResLexicalAnalysis> getLexical_analysis() {
		return lexical_analysis;
	}
	public void setLexical_analysis(
			List<SessionBotResLexicalAnalysis> lexical_analysis) {
		this.lexical_analysis = lexical_analysis;
	}
	public SessionBotResSentimentAnalysis getSentiment_analysis() {
		return sentiment_analysis;
	}
	public void setSentiment_analysis(
			SessionBotResSentimentAnalysis sentiment_analysis) {
		this.sentiment_analysis = sentiment_analysis;
	}
	public SessionBotRes() {
		super();
	}
	
	
}
