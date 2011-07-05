/*
 *   Project: Speedith.Core
 * 
 * File name: Goals.java
 *    Author: Matej Urbas [matej.urbas@gmail.com]
 * 
 *  Copyright © 2011 Matej Urbas
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
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package speedith.core.reasoning;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import speedith.core.lang.SpiderDiagram;
import static speedith.core.i18n.Translations.*;

/**
 * This class contains a list of spider diagrams.
 * <p>This list represents the goals that have to be proved in a {@link Proof}
 * by applying {@link InferenceRule inference rules} on them.</p>
 * <p>Instances of this class (and its derived classes) are immutable.</p>
 * @author Matej Urbas [matej.urbas@gmail.com]
 */
public class Goals {

    // <editor-fold defaultstate="collapsed" desc="Fields">
    private List<SpiderDiagram> goals;
    // </editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Constructor">
    /**
     * Initialises an instance of this class with a list of spider diagrams (the
     * goals/proof obligations).
     * <p>This constructor makes a copy of the given list. See {@link Goals#createGoalsFrom(java.util.List)}
     * and {@link Goals#createGoalsFrom(speedith.core.lang.SpiderDiagram[])} for
     * way of creating an instance of Goals without making a copy of the list.
     * Be warned, however, that you <span style="font-weight:bold">must not</span>
     * change the contents of the list of spider diagrams afterwards or you
     * risk erroneous behaviour.</p>
     * @param goals the list of spider diagrams which represent the proof goals
     * (proof obligations).
     */
    public Goals(Collection<SpiderDiagram> goals) {
        this(goals == null || goals.isEmpty() ? null : new ArrayList<SpiderDiagram>(goals), false);
    }

    /**
     * Initialises an instance of this class with a list of spider diagrams (the
     * goals/proof obligations).
     * <p><span style="font-weight:bold">Warning</span>: this method does not
     * make a copy of the list. Make sure you do not change the contents of the
     * list afterwards.</p>
     * @param goals the list of spider diagrams which represent the proof goals
     * (proof obligations).
     * @param isUnmodifiable indicates whether the list is unmodifiable already
     * or not.
     */
    Goals(List<SpiderDiagram> goals, boolean isUnmodifiable) {
        if (goals == null || goals.isEmpty()) {
            this.goals = null;
        } else {
            this.goals = isUnmodifiable ? goals : Collections.unmodifiableList(goals);
        }
    }

    /**
     * Initialises an instance of this class with a list of spider diagrams (the
     * goals/proof obligations).
     * <p><span style="font-weight:bold">Warning</span>: this method does not
     * make a copy of the list. Make sure you do not change the contents of the
     * list afterwards.</p>
     * @param goals the list of spider diagrams which represent the proof goals
     * (proof obligations).
     */
    Goals(SpiderDiagram[] goals) {
        this(goals == null || goals.length < 1 ? null : Arrays.asList(goals), false);
    }
    //</editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Public Properties">
    /**
     * Returns an unmodifiable list of spider diagrams that represent the proof
     * goals (proof obligations).
     * <p>Note: this method may return {@code null} to indicate that there are
     * not goals (proof obligations) left.</p>
     * @return an unmodifiable list of spider diagrams that represent the proof
     * goals (proof obligations).
     * <p>Note: this method may return {@code null} to indicate that there are
     * not goals (proof obligations) left.</p>
     */
    @SuppressWarnings("ReturnOfCollectionOrArrayField")
    public List<SpiderDiagram> getGoals() {
        return goals;
    }

    /**
     * Returns the spider diagram goal (proof obligation) at the given index.
     * <p>Note: you should check the number of goals before calling this
     * function.</p>
     * <p>To iterate over goals, see the method {@link Goals#getGoals()}.</p>
     * @param index the index of the goal to return.
     * @return a spider diagram (the goal at the given index).
     */
    public SpiderDiagram getGoalAt(int index) {
        if (goals == null) {
            throw new IndexOutOfBoundsException(i18n("GERR_INDEX_OUT_OF_BOUNDS"));
        }
        return goals.get(index);
    }

    /**
     * Returns the number of goals in this list.
     * @return the number of goals in this list.
     */
    public int getGoalsCount() {
        return goals == null || goals.isEmpty() ? 0 : goals.size();
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Factory Methods">
    /**
     * Creates a new instance of the {@link Goals} class with the given list of
     * spider diagrams as the proof goals (proof obligations).
     * <p><span style="font-weight:bold">Warning</span>: this method does not
     * make a copy of the list. Make sure you do not change the contents of the
     * list afterwards.</p>
     * @param goals the list of spider diagrams which represent the proof goals
     * (proof obligations).
     * @return the new instance of the {@link Goals} class. 
     */
    public static Goals createGoalsFrom(List<SpiderDiagram> goals) {
        return new Goals(goals);
    }

    /**
     * Creates a new instance of the {@link Goals} class with the given list of
     * spider diagrams as the proof goals (proof obligations).
     * <p><span style="font-weight:bold">Warning</span>: this method does not
     * make a copy of the list. Make sure you do not change the contents of the
     * list afterwards.</p>
     * @param goals the list of spider diagrams which represent the proof goals
     * (proof obligations).
     * @return the new instance of the {@link Goals} class. 
     */
    public static Goals createGoalsFrom(SpiderDiagram... goals) {
        return new Goals(goals);
    }

    /**
     * Creates a new instance of the {@link Goals} class with the given list of
     * spider diagrams as the proof goals (proof obligations).
     * <p><span style="font-weight:bold">Warning</span>: this method does not
     * make a copy of the list. Make sure you do not change the contents of the
     * list afterwards.</p>
     * <p><span style="font-weight:bold">Note</span>: this is a bit of an unsafe
     * method. Use it with care.</p>
     * @param goals the list of spider diagrams which represent the proof goals
     * (proof obligations).
     * @param isUnmodifiable indicates whether the given list of goals is
     * unmodifiable (i.e.: has been wrapped with the {@link Collections#unmodifiableList(java.util.List)}
     * already).
     * @return the new instance of the {@link Goals} class. 
     */
    public static Goals createGoalsFrom(List<SpiderDiagram> goals, boolean isUnmodifiable) {
        return new Goals(goals, isUnmodifiable);
    }
    // </editor-fold>
}
