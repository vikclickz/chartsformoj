package com.sdsu.edu.main;

import com.esri.mo2.cs.geom.BasePointsArray;
import com.esri.mo2.data.feat.BaseDataID;
import com.esri.mo2.data.feat.BaseFeature;
import com.esri.mo2.data.feat.BaseFeatureClass;
import com.esri.mo2.data.feat.BaseField;
import com.esri.mo2.data.feat.BaseFields;
import com.esri.mo2.data.feat.Field;
import com.esri.mo2.data.feat.MapDataset;
import com.esri.mo2.map.dpy.BaseFeatureLayer;
import com.esri.mo2.map.draw.BaseSimpleRenderer;
import com.esri.mo2.map.draw.TrueTypeMarkerSymbol;
import com.esri.mo2.ui.bean.Map;
import java.awt.Color;
import java.awt.Font;
import java.util.Vector;

public class XYfeatureLayer extends BaseFeatureLayer {
  BaseFields fields;
  private Vector<BaseFeature> featureVector;

  public XYfeatureLayer(BasePointsArray bpa, Map map, Vector<String> s2) {
      createFeaturesAndFields(bpa, map, s2);

      BaseFeatureClass bfc = getFeatureClass("MyPoints", bpa);
      setFeatureClass(bfc);
      BaseSimpleRenderer srd = new BaseSimpleRenderer();

      //SimpleMarkerSymbol sms = new SimpleMarkerSymbol();
      //sms.setType(SimpleMarkerSymbol.CROSS_MARKER);

      TrueTypeMarkerSymbol ttm = new TrueTypeMarkerSymbol();

      //sms.setSymbolColor(new Color(255, 0, 0));
      //sms.setWidth(5);

      ttm.setFont(new Font("ESRI Geology USGS 95-525", Font.PLAIN, 20));
      ttm.setColor(new Color(255, 0, 0));
      ttm.setCharacter("103");

      srd.setSymbol(ttm);

      //srd.setSymbol(sms);

      setRenderer(srd);
      // without setting layer capabilities, the points will not
      // display (but the toc entry will still appear)
      XYLayerCapabilities lc = new XYLayerCapabilities();
      setCapabilities(lc);

  }

  private void createFeaturesAndFields(BasePointsArray bpa, Map map,
          Vector<String> s2) {
      featureVector = new Vector<BaseFeature>();
      fields = new BaseFields();
      createDbfFields();
      int j = 0;
      for (int i = 0; i < bpa.size(); i++) {
          BaseFeature feature = new BaseFeature(); // feature is a row
          feature.setFields(fields);
          com.esri.mo2.cs.geom.Point p = new com.esri.mo2.cs.geom.Point(bpa.getPoint(i));

          feature.setValue(0, p);
          // feature.setValue(1, new Integer(0)); // point data
          feature.setValue(1, s2.elementAt(j));
          feature.setValue(2, s2.elementAt(j + 1));
          feature.setValue(3, s2.elementAt(j + 2));
          feature.setValue(4, s2.elementAt(j + 3));
          feature.setValue(5, s2.elementAt(j + 4));
          feature.setValue(6, s2.elementAt(j + 5));
          feature.setValue(7, s2.elementAt(j + 6));
          feature.setValue(8, s2.elementAt(j + 7));
          feature.setValue(9, s2.elementAt(j + 8));
          feature.setDataID(new BaseDataID("MyPoints", i));
          featureVector.addElement(feature);
          j = j + 9;
      }
  }

  private void createDbfFields() {
      fields.addField(new BaseField("#Shape#", Field.ESRI_SHAPE, 0, 0));
      fields.addField(new BaseField("Name", java.sql.Types.VARCHAR, 30, 0));
      fields.addField(new BaseField("President", java.sql.Types.VARCHAR, 10, 0));
      fields.addField(new BaseField("Vice President", java.sql.Types.VARCHAR, 50, 0));
      fields.addField(new BaseField("Phone", java.sql.Types.VARCHAR, 16, 0));
      fields.addField(new BaseField("Email", java.sql.Types.VARCHAR, 30, 0));
      fields.addField(new BaseField("Team", java.sql.Types.VARCHAR, 3, 0));
      fields.addField(new BaseField("URL", java.sql.Types.VARCHAR, 30, 0));
      fields.addField(new BaseField("Photo", java.sql.Types.VARCHAR, 3, 0));
      fields.addField(new BaseField("Video", java.sql.Types.VARCHAR, 30, 0));
  }

  public BaseFeatureClass getFeatureClass(String name, BasePointsArray bpa) {
      com.esri.mo2.map.mem.MemoryFeatureClass featClass = null;
      try {
          featClass = new com.esri.mo2.map.mem.MemoryFeatureClass(MapDataset.POINT, fields);
      } catch (IllegalArgumentException iae) {
      }
      featClass.setName(name);
      for (int i = 0; i < bpa.size(); i++) {
          featClass.addFeature(featureVector.elementAt(i));
      }
      return featClass;
  }

  public final class XYLayerCapabilities extends
  com.esri.mo2.map.dpy.LayerCapabilities {
      XYLayerCapabilities() {
          for (int i = 0; i < this.size(); i++) {
              setAvailable(this.getCapabilityName(i), true);
              setEnablingAllowed(this.getCapabilityName(i), true);
              getCapability(i).setEnabled(true);
          }
      }
  }
}
