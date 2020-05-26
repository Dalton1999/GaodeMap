package com.example.gaodemap;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.amap.api.location.AMapLocation;
import com.amap.api.maps2d.AMap;
import com.amap.api.maps2d.CameraUpdateFactory;
import com.amap.api.maps2d.LocationSource;
import com.amap.api.maps2d.MapView;
import com.amap.api.maps2d.model.LatLng;

public class MainActivity extends AppCompatActivity implements LocationSource{

    private MapView mapview;
    private AMap aMap;
    private LocationSource.OnLocationChangedListener mListener;//定位监听器
    private LocationUtil locationUtil;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mapview = (MapView) findViewById(R.id.mapview);
        mapview.onCreate(savedInstanceState);

        setLocationMap();
    }

    /**
     * 设置地图定位属性
     */
    private void setLocationMap() {
        if (aMap == null){
            aMap=mapview.getMap();
        }
        setLocationCallBack();

//        aMap.setMapType(AMap.MAP_TYPE_NORMAL);

        //设置定位监听
        aMap.setLocationSource(this);
        //设置缩放级别
        aMap.moveCamera(CameraUpdateFactory.zoomTo(15));
        //设置缩放级别
        aMap.setMyLocationEnabled(true);
    }

    /**
     * 根据经纬度调用地图定位
     */
    private void setLocationCallBack() {
        locationUtil=new LocationUtil();
        locationUtil.setLocationCallBack(new LocationUtil.ILocationCallBack() {
            @Override
            public void callBack(String str, double lat, double lgt, AMapLocation aMapLocation) {
                //根据获取的经纬度，将地图移动到定位位置
                aMap.moveCamera(CameraUpdateFactory.changeLatLng(new LatLng(lat,lgt)));
                mListener.onLocationChanged(aMapLocation);
                //添加定位图标
                aMap.addMarker(locationUtil.getMarkerOptions(str, lat, lgt));
            }
        });

    }

    //定位激活回调
    @Override
    public void activate(OnLocationChangedListener onLocationChangedListener) {
        mListener=onLocationChangedListener;
        locationUtil.startLocate(getApplicationContext());
    }

    @Override
    public void deactivate() {
        mListener=null;
    }

    @Override
    protected void onPause() {
        super.onPause();
        //暂停地图的绘制
        mapview.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //销毁地图
        mapview.onDestroy();
    }

    @Override
    protected void onResume() {
        super.onResume();
        //重新绘制加载地图
        mapview.onResume();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mapview.onSaveInstanceState(outState);
    }
}
