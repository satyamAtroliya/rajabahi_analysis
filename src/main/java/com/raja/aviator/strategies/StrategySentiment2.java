package com.raja.aviator.strategies;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class StrategySentiment2 implements Strategy {

        private static final Logger log =
                LoggerFactory.getLogger(StrategySentiment2.class);

        /*
         * A gap <= 10 produced the strongest useful cluster signal
         * in the historical dataset.
         */
        private static final int CLUSTER_GAP = 10;

        /*
         * Maximum number of rounds we bet after detecting the cluster.
         */
        private static final int BET_WINDOW = 30;

        /*
         * Number of completed non-100x rounds since the last 100x.
         *
         * -1 = no 100x seen yet.
         */
        private int roundsSince100x = -1;

        /*
         * Whether we're currently inside a cluster betting window.
         */
        private boolean clusterActive = false;

        /*
         * How many rounds of the current cluster window
         * have already been consumed.
         */
        private int clusterRound = 0;

        /*
         * Useful for logs / debugging.
         */
        private int lastGap = -1;

        private long total100x = 0;

        private long clustersDetected = 0;

        private long clusterHits = 0;


        /**
         * Call this AFTER every completed round.
         *
         * @param multiplier multiplier of the completed round
         * @return true if we should bet on the NEXT round
         */
        public boolean decisionMaker(double multiplier) {

            /*
             * ---------------------------------------------------
             * 100x EVENT
             * ---------------------------------------------------
             */
            if (multiplier >= 100.0) {

                total100x++;

                /*
                 * If we had seen a previous 100x,
                 * calculate the gap.
                 */
                if (roundsSince100x >= 0) {

                    lastGap = roundsSince100x;

                    log.debug(
                            "100x detected. Previous 100x gap = {}",
                            lastGap
                    );

                    /*
                     * If we were already betting because of
                     * a cluster, this 100x is a successful hit.
                     */
                    if (clusterActive) {

                        clusterHits++;

                        log.info(
                                "100x CLUSTER HIT. " +
                                        "Gap={} clusterRound={}",
                                lastGap,
                                clusterRound
                        );
                    }

                    /*
                     * ------------------------------------------------
                     * IMPORTANT PART
                     *
                     * Detect whether THIS new 100x itself forms
                     * another short-gap cluster.
                     *
                     * Example:
                     *
                     * 100x
                     *  7 rounds
                     * 100x  <- activate
                     *
                     * If another 100x comes 4 rounds later:
                     *
                     * 100x
                     *  4 rounds
                     * 100x  <- activate AGAIN
                     *
                     * This lets us ride a real cluster instead of
                     * stopping after one hit.
                     * ------------------------------------------------
                     */
                    if (lastGap <= CLUSTER_GAP) {

                        activateCluster();

                    } else {

                        deactivateCluster(
                                "100x gap was too large: " + lastGap
                        );
                    }
                }

                /*
                 * This current round becomes the new reference 100x.
                 */
                roundsSince100x = 0;

                /*
                 * If cluster was activated by THIS 100x,
                 * bet on the NEXT round.
                 */
                return clusterActive;
            }


            /*
             * ---------------------------------------------------
             * BEFORE FIRST 100x
             * ---------------------------------------------------
             */
            if (roundsSince100x < 0) {
                return false;
            }


            /*
             * Another normal round passed.
             */
            roundsSince100x++;


            /*
             * ---------------------------------------------------
             * NO ACTIVE CLUSTER
             * ---------------------------------------------------
             */
            if (!clusterActive) {
                return false;
            }


            /*
             * ---------------------------------------------------
             * ACTIVE CLUSTER
             * ---------------------------------------------------
             */

            clusterRound++;


            /*
             * Stop after the configured betting window.
             */
            if (clusterRound >= BET_WINDOW) {

                log.debug(
                        "Cluster expired after {} rounds without 100x.",
                        BET_WINDOW
                );

                deactivateCluster("Bet window completed");

                return false;
            }


            /*
             * Bet next round.
             */
            return true;
        }


        /**
         * Starts/restarts cluster mode.
         */
        private void activateCluster() {

            clusterActive = true;

            /*
             * Zero means the next round will be
             * cluster betting round #1.
             */
            clusterRound = 0;

            clustersDetected++;

            log.info(
                    "100x CLUSTER DETECTED. gap={}." +
                            " Betting next {} rounds.",
                    lastGap,
                    BET_WINDOW
            );
        }


        /**
         * Stops cluster betting.
         */
        private void deactivateCluster(String reason) {

            if (clusterActive) {

                log.debug(
                        "100x cluster stopped. Reason={}",
                        reason
                );
            }

            clusterActive = false;
            clusterRound = 0;
        }


        /*
         * ---------------------------------------------------
         * GETTERS
         * ---------------------------------------------------
         */

        public boolean isClusterActive() {
            return clusterActive;
        }

        public int getRoundsSince100x() {
            return roundsSince100x;
        }

        public int getClusterRound() {
            return clusterRound;
        }

        public int getLastGap() {
            return lastGap;
        }

        public long getTotal100x() {
            return total100x;
        }

        public long getClustersDetected() {
            return clustersDetected;
        }

        public long getClusterHits() {
            return clusterHits;
        }


        /**
         * Useful when starting a new dataset/session.
         */
        public void reset() {

            roundsSince100x = -1;

            clusterActive = false;

            clusterRound = 0;

            lastGap = -1;

            total100x = 0;

            clustersDetected = 0;

            clusterHits = 0;
        }
    }