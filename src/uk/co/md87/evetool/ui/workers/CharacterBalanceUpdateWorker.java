/*
 * Copyright (c) 2009 Chris Smith
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

package uk.co.md87.evetool.ui.workers;

import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.SwingWorker;

import uk.co.md87.evetool.api.ApiResponse;
import uk.co.md87.evetool.api.EveApi;
import uk.co.md87.evetool.api.wrappers.CharacterSheet;
import uk.co.md87.evetool.ui.data.AccountChar;

/**
 *
 * @author chris
 */
public class CharacterBalanceUpdateWorker extends
        SwingWorker<ApiResponse<CharacterSheet>, Object> {

    private final EveApi api;
    private final AccountChar character;

    public CharacterBalanceUpdateWorker(final EveApi api, final AccountChar character) {
        this.api = api;
        this.character = character;
    }

    /** {@inheritDoc} */
    @Override
    protected ApiResponse<CharacterSheet> doInBackground() throws Exception {
        final ApiResponse<CharacterSheet> res = api.getCharacterSheet();

        if (res.wasSuccessful()) {
            res.getResult().associateSkills(api.getSkillTree().getResult());
        }

        return res;
    }

    /** {@inheritDoc} */
    @Override
    protected void done() {
        try {
            character.setSheet(get());
        } catch (Exception ex) {            
            Logger.getLogger(CharacterBalanceUpdateWorker.class.getName())
                    .log(Level.SEVERE, "Error getting char sheet", ex);
        }
    }

}
