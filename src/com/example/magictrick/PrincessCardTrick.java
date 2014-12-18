package com.example.magictrick;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.vaadin.artur.playingcards.Card;
import org.vaadin.artur.playingcards.CardPile;
import org.vaadin.artur.playingcards.Deck;
import org.vaadin.artur.playingcards.client.ui.Suite;

import com.github.wolfie.refresher.Refresher;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Component;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Layout;
import com.vaadin.ui.VerticalLayout;

public class PrincessCardTrick extends VerticalLayout{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static final int NUMBER_PILES = 5;
	private static final String INITIAL_BUTTON_NAME = "Start";
	private int actionNumber = 0;
	private final List<CardPile> piles;
	private Deck deck = new Deck();
	private List<MyXCard> helpDeck;
	private List<MyXCard> originCards;
	private List<MyXCard> similarCards;
	private Label message = new Label();
	private Button nextActionButton =
			new Button(INITIAL_BUTTON_NAME);
	private final Refresher refresher = new Refresher();

	public PrincessCardTrick() {
		setMargin(true);
		setSpacing(true);
		refresher.setRefreshInterval(500);
		addExtension(refresher);
		addComponent(deck);
		Layout cardLayout = createCardLayout();
		addComponent(cardLayout);
		piles = createPiles(NUMBER_PILES, cardLayout);
		addComponent(createMessageLayout());
		initCards();
	}

	private void initCards() {
		for (CardPile pile : piles) {
			pile.removeAllCards();
		}
		helpDeck = createHelpDeck();
		cleanDeck(deck);
		originCards = new ArrayList<MyXCard>();
		similarCards = new ArrayList<MyXCard>();
		for (int i = 0; i < NUMBER_PILES; i++) {
			addOriginAndSimilarCard();
			CardPile pile = piles.get(i);
			pile.addCard(originCards.get(i));
		}
		message.setValue("Remember one card and press button");

	}

	private void cleanDeck(Deck deck) {
		final int size = deck.size();
		for (int i = 0; i < size; i++) {
			deck.removeTopCard();
		}

	}

	private List<MyXCard> createHelpDeck() {
		final List<MyXCard> cards = new ArrayList<MyXCard>();
		for (final Suite suite : Suite.values()) {
			for (int i = 1; i <= 13; i++) {
				MyXCard card = new MyXCard(suite, i);
				cards.add(card);
			}
		}
		return cards;
	}

	private void addOriginAndSimilarCard() {
		Collections.shuffle(helpDeck);
		MyXCard originCard = helpDeck.remove(0);
		MyXCard similarCard = findSimilarCard(originCard);
		if (helpDeck.remove(similarCard)) {
			originCards.add(originCard);
			similarCards.add(similarCard);
		} else {
			helpDeck.add(originCard);
			addOriginAndSimilarCard();
		}

	}

	private MyXCard findSimilarCard(final Card originCard) {
		final Suite suite = originCard.getSuite();
		Suite similarSuite = null;
		switch (suite) {
		case HEARTS:
			similarSuite = Suite.DIAMONDS;
			break;
		case DIAMONDS:
			similarSuite = Suite.HEARTS;
			break;
		case CLUBS:
			similarSuite = Suite.SPADES;
			break;
		case SPADES:
			similarSuite = Suite.CLUBS;
			break;
		}
		int rank = originCard.getRank() - 1;
		if (rank < 0) {
			rank = 2; }
		if (rank == 10) {
			rank = 13;
		}
		return new MyXCard(similarSuite, rank);
	}

	private List<CardPile> createPiles(int number, Layout layout) {
		List<CardPile> piles = new ArrayList<CardPile>();
		for (int i = 0; i < number; i++) {
			CardPile pile = new CardPile();
			piles.add(pile);
			layout.addComponent(pile);
		}
		return piles;
	}

	private Component createMessageLayout() {
		HorizontalLayout messageLayout = new HorizontalLayout();
		messageLayout.setSpacing(true);
		messageLayout.setSizeFull();
		nextActionButton.addClickListener(new ClickListener() {


			@Override
			public void buttonClick(ClickEvent event) {
				nextAction();
			}
		});
		messageLayout.addComponent(message);
		messageLayout.addComponent(nextActionButton);
		return messageLayout;
	}

	private void nextAction() {
		actionNumber++;
		switch (actionNumber) {
		case 1:
			message.setValue("Shuffling cards...");
			final Thread animation = new Thread(
					new Animation(originCards, similarCards,
							piles, deck, message, nextActionButton));
			animation.start();
			nextActionButton.setVisible(false);
			break;
		case 2:
			actionNumber = 0;
			nextActionButton.setCaption(INITIAL_BUTTON_NAME);
			initCards();
			break;
		} 
	}



	private Layout createCardLayout() {
		HorizontalLayout cardLayout = new HorizontalLayout();
		cardLayout.setSpacing(true);
		return cardLayout;
	}
}