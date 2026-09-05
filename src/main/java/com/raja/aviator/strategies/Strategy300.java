package com.raja.aviator.strategies;

public class Strategy300 implements Strategy {

        private int roundsSince100x = -1;

        private int previousGap = -1;

        private boolean active = false;


        public boolean decisionMaker(double multiplier) {

            /*
             * New 100x+ event
             */
            if (multiplier >= 100.0) {

                if (roundsSince100x >= 0) {

                    previousGap = roundsSince100x;

                    /*
                     * Activate strategy only when the
                     * previous gap was >= 300 rounds.
                     */
                    active = previousGap >= 300;
                }

                roundsSince100x = 0;

                return false;
            }


            /*
             * No 100x seen yet
             */
            if (roundsSince100x < 0) {
                return false;
            }


            roundsSince100x++;


            /*
             * =========================================================
             * LONG GAP >= 300
             *
             * Bet only rounds 21-26
             * =========================================================
             */
            if (active) {

                if (roundsSince100x >= 21
                        && roundsSince100x <= 26) {

                    return true;
                }

                /*
                 * Stop after round 26.
                 */
                if (roundsSince100x > 26) {
                    active = false;
                }

                return false;
            }


            return false;
        }


        public int getRoundsSince100x() {
            return roundsSince100x;
        }


        public int getPreviousGap() {
            return previousGap;
        }


        public boolean isActive() {
            return active;
        }
    }