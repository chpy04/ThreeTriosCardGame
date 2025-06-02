We only attempted level 0 and 1 of the extra credit. To do this, we made some slight changes to our
GridPanel interface which allowed us to add the HintDecorator class which is also a GridPanel but
can now take in another GridPanel. The HintDecorator addes hints to the appropriate cards on top
of whatever delegate it is given. To use these classes, the JFrameView keeps track of whether or
not it is displaying hints for its player, and changes whether hints are shown when the user presses
the key 'h'. If hints are show, then when the page is refreshed the JFrameView creates a
HintDecorator with the existing GridPanel as the delegate, and refreshes that GridPanel. Within
the HintDecorator, hints are only displayed if there is currently a selected card and the move
is legal.

For Level 1, we decided that instead of creating new models, the only functionality that actually
needed to be changed was how attack values are compared when cards were battling. Because of this,
we decided to add methods to our board and cards, which could add a filter function that would
process attack values before battling. For example, if the reverse filter function as passed into
the model, it would pass it through to all of it's cards. Then, any time battle was called ont the
cards, the card would first pass it's attack values through a function that reversed the value,
and then compare them. This also meant that only the filter functionality on the attack values need
be tested.
