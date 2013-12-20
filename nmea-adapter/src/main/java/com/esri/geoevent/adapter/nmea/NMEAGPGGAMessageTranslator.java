/*
  Copyright 1995-2013 Esri

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.

  For additional information, contact:
  Environmental Systems Research Institute, Inc.
  Attn: Contracts Dept
  380 New York Street
  Redlands, California, USA 92373

  email: contracts@esri.com
*/

package com.esri.geoevent.adapter.nmea;

import java.nio.ByteBuffer;

import com.esri.ges.core.geoevent.FieldException;
import com.esri.ges.core.geoevent.GeoEvent;
import com.esri.ges.spatial.Point;
import com.esri.ges.spatial.Spatial;

public class NMEAGPGGAMessageTranslator extends NMEAMessageTranslator
{
  @Override
  public void translate(String trackId, ByteBuffer buffer, GeoEvent geoEvent, Spatial spatial) throws FieldException
  {
    byte[] dst = new byte[buffer.remaining()];
    buffer.get(dst, 0, buffer.remaining());
    String[] data = new String(dst).split(",");

    int i = 0;
    geoEvent.setField(i++, trackId);
    geoEvent.setField(i++, toTime(data[1], null));
    double lat_d = Double.parseDouble(data[2].substring(0, 2));
    double lat_m = Double.parseDouble(data[2].substring(2, data[2].length()));
    double lat = lat_d + (lat_m / 60.0);
    double lon_d = Double.parseDouble(data[4].substring(0, 3));
    double lon_m = Double.parseDouble(data[4].substring(3, data[4].length()));
    double lon = lon_d + (lon_m / 60.0);
    lat = (data[3].equals("N")) ? lat : -lat;
    lon = (data[5].equals("E")) ? lon : -lon;
    Point point = spatial.createPoint(lon, lat, 4326);
    geoEvent.setField(i++, point);
    geoEvent.setField(i++, Short.parseShort(data[6]));
    geoEvent.setField(i++, Short.parseShort(data[7]));
    geoEvent.setField(i++, Double.parseDouble(data[8]));
    geoEvent.setField(i++, Double.parseDouble(data[9]));
    geoEvent.setField(i++, data[10]);
    geoEvent.setField(i++, Double.parseDouble(data[11]));
    geoEvent.setField(i++, data[12]);
    try
    {
      geoEvent.setField(i, Double.parseDouble(data[13]));
      i++;
    }
    catch (NumberFormatException ex)
    {
      i++;
    }
    geoEvent.setField(i++, data[14].split("\\*")[0]);
  }
}
