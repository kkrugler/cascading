/*
 * Copyright (c) 2007-2013 Concurrent, Inc. All Rights Reserved.
 *
 * Project and contact information: http://www.cascading.org/
 *
 * This file is part of the Cascading project.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package cascading.tuple.collect;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import cascading.property.Props;
import cascading.util.Util;

/**
 * Class SpillableProps is a fluent interface for building properties to be passed to a
 * {@link cascading.flow.FlowConnector} before creating new {@link cascading.flow.Flow} instances.
 *
 * @see SpillableTupleList
 * @see SpillableTupleMap
 */
public class SpillableProps extends Props
  {
  /**
   * Whether to enable compress of the spills or not, on by default.
   *
   * @see Boolean#parseBoolean(String)
   */
  public static final String SPILL_COMPRESS = "cascading.spill.compress";

  /** A comma delimited list of possible codecs to try. This is platform dependent. */
  public static final String SPILL_CODECS = "cascading.spill.codecs";

  /** Number of tuples to hold in memory before spilling them to disk. */
  public static final String LIST_THRESHOLD = "cascading.spill.list.threshold";

  /** The total number of tuple values (not keys) to attempt to keep in memory. */
  public static final String MAP_THRESHOLD = "cascading.spill.map.threshold";

  /**
   * The initial hash map capacity.
   *
   * @see java.util.HashMap
   */
  public static final String MAP_CAPACITY = "cascading.spill.map.capacity";

  /**
   * The initial hash map load factor.
   *
   * @see java.util.HashMap
   */
  public static final String MAP_LOADFACTOR = "cascading.spill.map.loadfactor";

  public static final int defaultListThreshold = 10 * 1000;

  public static final int defaultMapThreshold = 10 * 1000;
  public static final int defaultMapInitialCapacity = 100 * 1000;
  public static final float defaultMapLoadFactor = 0.75f;

  boolean compressSpill = true;
  List<String> codecs = new ArrayList<String>();

  int listSpillThreshold = defaultListThreshold;

  int mapSpillThreshold = defaultMapThreshold;
  int mapInitialCapacity = defaultMapInitialCapacity;
  float mapLoadFactor = defaultMapLoadFactor;

  public static SpillableProps spillableProps()
    {
    return new SpillableProps();
    }

  public SpillableProps()
    {
    }

  public boolean isCompressSpill()
    {
    return compressSpill;
    }

  public SpillableProps setCompressSpill( boolean compressSpill )
    {
    this.compressSpill = compressSpill;

    return this;
    }

  public List<String> getCodecs()
    {
    return codecs;
    }

  public SpillableProps setCodecs( List<String> codecs )
    {
    this.codecs = codecs;

    return this;
    }

  public SpillableProps addCodecs( List<String> codecs )
    {
    this.codecs.addAll( codecs );

    return this;
    }

  public SpillableProps addCodec( String codec )
    {
    this.codecs.add( codec );

    return this;
    }

  public int getListSpillThreshold()
    {
    return listSpillThreshold;
    }

  public SpillableProps setListSpillThreshold( int listSpillThreshold )
    {
    this.listSpillThreshold = listSpillThreshold;

    return this;
    }

  public int getMapSpillThreshold()
    {
    return mapSpillThreshold;
    }

  public SpillableProps setMapSpillThreshold( int mapSpillThreshold )
    {
    this.mapSpillThreshold = mapSpillThreshold;

    return this;
    }

  public int getMapInitialCapacity()
    {
    return mapInitialCapacity;
    }

  public SpillableProps setMapInitialCapacity( int mapInitialCapacity )
    {
    this.mapInitialCapacity = mapInitialCapacity;

    return this;
    }

  public float getMapLoadFactor()
    {
    return mapLoadFactor;
    }

  public SpillableProps setMapLoadFactor( float mapLoadFactor )
    {
    this.mapLoadFactor = mapLoadFactor;

    return this;
    }

  @Override
  protected void addPropertiesTo( Properties properties )
    {
    for( String codec : codecs )
      {
      String codecs = (String) properties.get( SPILL_CODECS );

      properties.put( SPILL_CODECS, Util.join( ",", Util.removeNulls( codecs, codec ) ) );
      }

    properties.setProperty( SPILL_COMPRESS, Boolean.toString( compressSpill ) );
    properties.setProperty( LIST_THRESHOLD, Integer.toString( listSpillThreshold ) );

    properties.setProperty( MAP_THRESHOLD, Integer.toString( mapSpillThreshold ) );
    properties.setProperty( MAP_CAPACITY, Integer.toString( mapInitialCapacity ) );
    properties.setProperty( MAP_LOADFACTOR, Float.toString( mapLoadFactor ) );
    }
  }
