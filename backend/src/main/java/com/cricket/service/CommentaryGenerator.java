package com.cricket.service;

import com.cricket.model.ExtraType;
import com.cricket.model.WicketType;
import org.springframework.stereotype.Component;

import java.util.Random;

@Component
public class CommentaryGenerator {

    private final Random random = new Random();

    private final String[] dotCommentaries = {
        "{bowler} hits a good length on off stump, defended solidly back down the pitch by {batsman}.",
        "Good seam movement from {bowler}, {batsman} shoulders arms and lets it go through to the keeper.",
        "Pushed towards point with soft hands, fielder attacks the ball quickly. No run.",
        "Beaten outside off! {bowler} gets that one to nip away off the seam, just past {batsman}'s outside edge.",
        "Back of a length angling into the body, tucked around the corner to short midwicket. No single there."
    };

    private final String[] singleCommentaries = {
        "{batsman} waits on the bounce and guides it down to third man for a comfortable single.",
        "Dropped with soft hands into the gap at covers, quick call and they scurry through for one.",
        "Turned off the hips towards deep square leg, single taken to rotate the strike.",
        "Pushed down to long-on against {bowler}, straightforward single taken.",
        "Tapped into the off side, good communication between the batters as they complete a quick run."
    };

    private final String[] twoRunsCommentaries = {
        "Clipped off the pads through midwicket! Good running between the wickets, they push hard and complete two.",
        "Driven nicely through the covers, sweeper runs around to cut it off, batters jog back for the second.",
        "Worked softly towards deep backward square leg, superb acceleration from {batsman} to steal a couple."
    };

    private final String[] threeRunsCommentaries = {
        "Glorious stroke through extra cover! The outfield slows it down just inside the rope, batters take three runs with energetic running!"
    };

    private final String[] fourCommentaries = {
        "FOUR! What a shot! {batsman} leans into an exquisite cover drive that races across the outfield to the fence!",
        "FOUR! Short and punished! {batsman} swivels and pulls {bowler} authoritatively through midwicket for a boundary!",
        "FOUR! Edged and past the slips! Flies between second slip and gully, race to the boundary won by the ball!",
        "FOUR! Beautiful presentation of the full face of the bat, straight driven past the bowler with pure timing!",
        "FOUR! Cut away with pinpoint precision! Back of a length delivery dispatched behind point for four runs!"
    };

    private final String[] sixCommentaries = {
        "SIX! Massive! {batsman} clears the front leg and lofts {bowler} miles into the stands over long-on!",
        "SIX! Out of the ground! Picked up off the pads and deposited deep into the crowd over backward square leg!",
        "SIX! What a strike! Dances down the track and hammers it with supreme disdain over deep midwicket!",
        "SIX! Audacious upper cut over third man! Flown all the way over the ropes for a sensational maximum!"
    };

    private final String[] bowledCommentaries = {
        "OUT! BOWLED HIM! Timber strikes! {bowler} delivers a searing yorker that crashes straight into the middle stump! {batsman} has to walk!",
        "OUT! CLEAN BOWLED! Deceived by the swing, takes the inside edge and disturbs the woodwork! Big breakthrough for the bowling side!"
    };

    private final String[] caughtCommentaries = {
        "OUT! CAUGHT! {batsman} tries to go over the top but miscues it completely! Fielder settles underneath and takes a safe catch!",
        "OUT! EDGED AND TAKEN! Beautiful outswinger from {bowler}, {batsman} hangs out the bat and the wicketkeeper takes a diving catch!"
    };

    private final String[] lbwCommentaries = {
        "OUT! LBW! Huge appeal from {bowler}... and the umpire's finger goes up! Plumb in front, no doubt about that one!",
        "OUT! Given out LBW! Trapped in front as the ball stays low, {batsman} was caught on the crease!"
    };

    private final String[] runOutCommentaries = {
        "OUT! RUN OUT! Chaos between the wickets! Direct hit from backward point and {batsman} is well short of his crease!",
        "OUT! RUN OUT! Hesitation in the middle, fielder gathers cleanly and whips the bails off!"
    };

    private final String[] stumpedCommentaries = {
        "OUT! STUMPED! {batsman} stepped out looking to loft down the ground, beaten in flight and the keeper whips the bails off in a flash!"
    };

    private final String[] wideCommentaries = {
        "Wide! {bowler} sprays it well down the leg side, wicketkeeper dives to collect. One extra run added.",
        "Wide! Fired down outside the off-stump tramline, easy decision for the umpire."
    };

    private final String[] noBallCommentaries = {
        "NO BALL! {bowler} has overstepped the popping crease! Free hit coming up next for {batsman}!",
        "NO BALL! High full toss above waist height! Umpire signals no-ball immediately."
    };

    public String generate(String batsman, String bowler, int runs, ExtraType extraType,
                           int extraRuns, boolean isWicket, WicketType wicketType) {
        String template;

        if (extraType == ExtraType.WIDE) {
            template = pickRandom(wideCommentaries);
        } else if (extraType == ExtraType.NO_BALL) {
            template = pickRandom(noBallCommentaries);
        } else if (isWicket) {
            if (wicketType == null) wicketType = WicketType.CAUGHT;
            switch (wicketType) {
                case BOWLED -> template = pickRandom(bowledCommentaries);
                case LBW -> template = pickRandom(lbwCommentaries);
                case RUN_OUT -> template = pickRandom(runOutCommentaries);
                case STUMPED -> template = pickRandom(stumpedCommentaries);
                case CAUGHT, HIT_WICKET -> template = pickRandom(caughtCommentaries);
                default -> template = pickRandom(caughtCommentaries);
            }
        } else {
            switch (runs) {
                case 0 -> template = pickRandom(dotCommentaries);
                case 1 -> template = pickRandom(singleCommentaries);
                case 2 -> template = pickRandom(twoRunsCommentaries);
                case 3 -> template = pickRandom(threeRunsCommentaries);
                case 4 -> template = pickRandom(fourCommentaries);
                case 6 -> template = pickRandom(sixCommentaries);
                default -> template = pickRandom(singleCommentaries);
            }
        }

        return template.replace("{batsman}", batsman != null ? batsman : "Batsman")
                       .replace("{bowler}", bowler != null ? bowler : "Bowler");
    }

    private String pickRandom(String[] array) {
        return array[random.nextInt(array.length)];
    }
}
