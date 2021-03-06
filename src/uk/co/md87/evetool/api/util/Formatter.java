/*
 * Copyright (c) 2006-2009 Chris Smith, Shane Mc Cormack, Gregory Holmes
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package uk.co.md87.evetool.api.util;

/**
 *
 * @author chris
 */
public class Formatter {

    /**
     * Tests for and adds one component of the duration format.
     *
     * @param builder The string builder to append text to
     * @param current The number of seconds in the duration
     * @param duration The number of seconds in this component
     * @param name The name of this component
     * @return The number of seconds used by this component
     */
    private static int doDuration(final StringBuilder builder, final int current,
            final int duration, final String name) {
        int res = 0;

        if (current >= duration) {
            final int units = current / duration;
            res = units * duration;

            if (builder.length() > 0) {
                builder.append(' ');
            }

            builder.append(units);
            builder.append(name);
        }

        return res;
    }

    /**
     * Formats the specified number of seconds as a string containing the
     * number of days, hours, minutes and seconds.
     *
     * @param duration The duration in seconds to be formatted
     * @return A textual version of the duration
     */
    public static String formatDuration(final int duration) {
        final StringBuilder buff = new StringBuilder();

        if (duration <= 0) {
            return "Done";
        }

        int seconds = duration, lseconds = duration;
        int parts = 4;

        if (parts > 0) {
            seconds -= doDuration(buff, seconds, 60*60*24, "d");
            parts -= seconds == lseconds ? 1 : 2;
            lseconds = seconds;
        }


        if (parts > 0) {
            seconds -= doDuration(buff, seconds, 60*60, "h");
            parts -= seconds == lseconds ? 1 : 2;
            lseconds = seconds;
        }

        if (parts > 0) {
            seconds -= doDuration(buff, seconds, 60, "m");
            parts -= seconds == lseconds ? 1 : 2;
            lseconds = seconds;
        }

        if (parts > 0) {
            seconds -= doDuration(buff, seconds, 1, "s");
            parts -= seconds == lseconds ? 1 : 2;
            lseconds = seconds;
        }

        return buff.toString();
    }

}
