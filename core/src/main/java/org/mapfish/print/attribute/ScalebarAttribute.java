/*
 * Copyright (C) 2014  Camptocamp
 *
 * This file is part of MapFish Print
 *
 * MapFish Print is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * MapFish Print is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with MapFish Print.  If not, see <http://www.gnu.org/licenses/>.
 */

package org.mapfish.print.attribute;


import com.google.common.base.Strings;

import org.mapfish.print.config.ConfigurationException;
import org.mapfish.print.config.Template;
import org.mapfish.print.map.DistanceUnit;
import org.mapfish.print.map.style.json.ColorParser;
import org.mapfish.print.parser.HasDefaultValue;
import org.mapfish.print.processor.map.scalebar.Orientation;
import org.mapfish.print.processor.map.scalebar.Type;

import java.awt.Color;
import java.awt.Dimension;
import java.util.List;

/**
 * The attributes for {@link org.mapfish.print.processor.map.scalebar.CreateScalebarProcessor}.
 */
public class ScalebarAttribute extends ReflectiveAttribute<ScalebarAttribute.ScalebarAttributeValues> {

    private Integer width = null;
    private Integer height = null;

    @Override
    public final void validate(final List<Throwable> validationErrors) {
        if (this.width == null || this.width < 1) {
            validationErrors.add(new ConfigurationException("width field is not legal: " + this.width + " in " + getClass().getName()));
        }

        if (this.height == null || this.height < 1) {
            validationErrors.add(new ConfigurationException("height field is not legal: " + this.height + " in " + getClass().getName()));
        }
    }

    @Override
    public final ScalebarAttributeValues createValue(final Template template) {
        return new ScalebarAttributeValues(new Dimension(this.width, this.height));
    }

    @Override
    protected final Class<? extends ScalebarAttributeValues> getValueType() {
        return ScalebarAttributeValues.class;
    }

    public final Integer getWidth() {
        return this.width;
    }

    public final void setWidth(final Integer width) {
        this.width = width;
    }

    public final Integer getHeight() {
        return this.height;
    }

    public final void setHeight(final Integer height) {
        this.height = height;
    }

    /**
     * The value of {@link ScalebarAttribute}.
     */
    public class ScalebarAttributeValues {

        private static final int DEFAULT_INTERVALS = 3;
        private static final String DEFAULT_FONT = "Helvetica";
        private static final int DEFAULT_FONT_SIZE = 12;
        private static final String DEFAULT_FONT_COLOR = "black";
        private static final String DEFAULT_COLOR = "black";
        private static final String DEFAULT_BAR_BG_COLOR = "white";
        private static final String DEFAULT_BACKGROUND_COLOR = "rgba(255, 255, 255, 0)";

        private final Dimension size;

        /**
         * The scalebar type. Available types: "line", "bar" (default) and "bar_sub". 
         */
        @HasDefaultValue
        public String type = Type.BAR.getLabel();

        /**
         * The unit to use.
         *
         * <p>The unit can be any of:</p>
         * <ul>
         *      <li>m (mm, cm, m or km)</li>
         *      <li>ft (in, ft, yd, mi)</li>
         *      <li>degrees (min, sec, °)</li>
         * </ul>
         *
         * <p>If the value is too big or too small, the module will switch to one of the unit in parenthesis
         * (the same unit is used for every interval). If this behaviour is not desired, the `lockUnits` parameter
         * will force the declared unit (or map unit if no unit is declared) to be used for the scalebar.</p>
         */
        @HasDefaultValue
        public String unit = null;

        /**
         * Force that the given unit is used (default: false).
         * For example if the unit is set to meters and `lockUnits` is enabled,
         * then meters is always used, even when kilometers would create nicer
         * values.
         */
        @HasDefaultValue
        public Boolean lockUnits = false;

        /**
         * The number of intervals (default: 3).
         */
        @HasDefaultValue
        public Integer intervals = DEFAULT_INTERVALS;

        /**
         * Should sub-intervals be shown? Default: false
         * The number of sub-intervals depends on the length of an interval.
         */
        @HasDefaultValue
        public Boolean subIntervals = false;

        /**
         * The thickness of the bar or the height of the tick marks on the line (in pixel).
         */
        @HasDefaultValue
        public Integer barSize = null;

        /**
         * The thickness of the lines or the bar border (in pixel).
         */
        @HasDefaultValue
        public Integer lineWidth = null;

        /**
         * The distance between scalebar and labels (in pixel).
         */
        @HasDefaultValue
        public Integer labelDistance = null;

        /**
         * The padding around the scalebar (in pixel).
         */
        @HasDefaultValue
        public Integer padding = null;

        /**
         * The font used for the labels (default: Helvetica).
         */
        @HasDefaultValue
        public String font = DEFAULT_FONT;

        /**
         * The font size (in pt) of the labels (default: 12).
         */
        @HasDefaultValue
        public Integer fontSize = DEFAULT_FONT_SIZE;

        /**
         * The font color of the labels (default: black).
         */
        @HasDefaultValue
        public String fontColor = DEFAULT_FONT_COLOR;

        /**
         * The color used to draw the bar and lines (default: black).
         */
        @HasDefaultValue
        public String color = DEFAULT_COLOR;

        /**
         * The color used to draw the alternating blocks for style "bar" and "bar_sub" (default: white).
         */
        @HasDefaultValue
        public String barBgColor = DEFAULT_BAR_BG_COLOR;

        /**
         * The background color for the scalebar graphic (default: rgba(255, 255, 255, 0)).
         */
        @HasDefaultValue
        public String backgroundColor = DEFAULT_BACKGROUND_COLOR;

        /**
         * The scalebar orientation.
         *
         * <p>Available options:</p>
         * <ul>
         *      <li>"horizontalLabelsBelow" (default): Horizontal scalebar and the labels are shown below the bar.</li>
         *      <li>"horizontalLabelsAbove": Horizontal scalebar and the labels are shown above the bar.</li>
         *      <li>"verticalLabelsLeft": Vertical scalebar and the labels are shown left of the bar.</li>
         *      <li>"verticalLabelsRight": Vertical scalebar and the labels are shown right of the bar.</li>
         * </ul>
         */
        @HasDefaultValue
        public String orientation = Orientation.HORIZONTAL_LABELS_BELOW.getLabel();

        /**
         * Constructor.
         *
         * @param size The size of the scalebar graphic in the Jasper report (in pixels).
         */
        public ScalebarAttributeValues(final Dimension size) {
            this.size = size;
        }

        /**
         * Initialize default values and validate that the config is correct.
         */
        public final void postConstruct() {
            if (getType() == null) {
                throw new IllegalArgumentException("invalid scalebar type: " + this.type);
            }
            if (this.unit != null && DistanceUnit.fromString(this.unit) == null) {
                throw new IllegalArgumentException("invalid unit: " + this.unit);
            }
            if (this.intervals < 2) {
                throw new IllegalArgumentException("invalid number of intervals: " + this.intervals);
            }
            if (this.color != null && !tryParseColor(this.color)) {
                throw new IllegalArgumentException("invalid color: " + this.color);
            }
            if (this.fontColor != null && !tryParseColor(this.fontColor)) {
                throw new IllegalArgumentException("invalid font color: " + this.fontColor);
            }
            if (this.barBgColor != null && !tryParseColor(this.barBgColor)) {
                throw new IllegalArgumentException("invalid bar background color: " + this.barBgColor);
            }
            if (this.backgroundColor != null && !tryParseColor(this.backgroundColor)) {
                throw new IllegalArgumentException("invalid background color: " + this.backgroundColor);
            }
            if (getOrientation() == null) {
                throw new IllegalArgumentException("invalid scalebar orientation: " + this.orientation);
            }
        }

        private boolean tryParseColor(final String colorString) {
            try {
                return ColorParser.toColor(colorString) != null;
            } catch (Exception exc) {
                return false;
            }
        }

        public final Dimension getSize() {
            return this.size;
        }

        public final Color getColor() {
            return ColorParser.toColor(this.color);
        }

        public final Color getFontColor() {
            return ColorParser.toColor(this.fontColor);
        }

        public final Color getBarBgColor() {
            return ColorParser.toColor(this.barBgColor);
        }

        public final Color getBackgroundColor() {
            return ColorParser.toColor(this.backgroundColor);
        }

        /**
         * @return Return the scalebar type.
         */
        public final Type getType() {
            if (Strings.isNullOrEmpty(this.type)) {
                return null;
            } else {
                return Type.fromString(this.type);
            }
        }

        /**
         * @return Return the unit to use for the scalebar.
         */
        public final DistanceUnit getUnit() {
            if (Strings.isNullOrEmpty(this.unit)) {
                return null;
            } else {
                return DistanceUnit.fromString(this.unit);
            }
        }

        /**
         * @return Return the scalebar orientation.
         */
        public final Orientation getOrientation() {
            if (Strings.isNullOrEmpty(this.orientation)) {
                return null;
            } else {
                return Orientation.fromString(this.orientation);
            }
        }
    }
}
