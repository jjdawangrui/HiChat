package com.itheima.hichat.view.activity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;

import com.fourmob.datetimepicker.date.DatePickerDialog;
import com.itheima.hichat.R;
import com.itheima.hichat.base.BaseActivity;
import com.itheima.hichat.modle.bean.User;
import com.lljjcoder.citypickerview.widget.CityPicker;

import java.io.File;
import java.io.FileOutputStream;
import java.io.Serializable;
import java.util.Calendar;

import butterknife.ButterKnife;
import cn.bmob.v3.datatype.BmobFile;

public class PersonalInfoActivity extends BaseActivity implements View.OnClickListener {

    private DatePickerDialog datePickerDialog;
    private User user;

    @Override
    public View addContent(LayoutInflater mInflater, FrameLayout content) {
        View view = mInflater.inflate(R.layout.activity_personal_info, content, true);
        return view;
    }

    ImageView ivIcon;
    TextView tvNickname;
    TextView tvBirthday;
    TextView tvHome;
    RadioButton rbMale;
    RadioButton rbFemale;
    Button btNext;

    @Override
    public void initContent(View view) {
        ivIcon = ButterKnife.findById(view, R.id.iv_icon);
        tvNickname = ButterKnife.findById(view, R.id.tv_nickname);
        tvBirthday = ButterKnife.findById(view, R.id.tv_birthday);
        tvHome = ButterKnife.findById(view, R.id.tv_home);
        rbMale = ButterKnife.findById(view, R.id.rb_male);
        rbFemale = ButterKnife.findById(view, R.id.rb_female);
        btNext = ButterKnife.findById(view, R.id.bt_next);

        btNext.setOnClickListener(this);
        ivIcon.setOnClickListener(this);
        tvHome.setOnClickListener(this);
        tvBirthday.setOnClickListener(this);
        rbMale.setOnClickListener(this);
        rbFemale.setOnClickListener(this);

        user = (User) getIntent().getSerializableExtra("user");
        tvNickname.setText(user.getNickname());
    }

    public void onClick(View view) {
        super.onClick(view);//让父类的onClick()得到调用，BaseActivity里面有返回键的onClick，很精髓
        switch (view.getId()) {
            case R.id.iv_icon:
                pickPictureFromSystemGallery();
                break;
            case R.id.tv_home:
                showSelectHomeDialog();
                break;
            case R.id.tv_birthday:
                showSelectDateDialog();
                break;
            case R.id.rb_male:
                changeSexCheckState(true);
                break;
            case R.id.rb_female:
                changeSexCheckState(false);
                break;
            case R.id.bt_next:
                showSexConfirmAlertDialog();
                break;
        }
    }

    //性别确认提示对话框
    private void showSexConfirmAlertDialog() {
        new AlertDialog.Builder(this)
                .setTitle("提示")
                .setMessage("注册成功后，性别不可以修改")
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //进入注册界面
                        Intent intent = new Intent(PersonalInfoActivity.this,RegisterActivity.class);
                        //获取信息（头像、生日、家乡、性别）
                        putDataToUser();
                        intent.putExtra("user",user);
                        startActivity(intent);
                    }
                })
                .setNegativeButton("取消",null)
                .show();
    }

    private void putDataToUser() {
        //头像、生日、家乡、性别
        File file = new File(getFilesDir(),user.getNickname()+".jpg");
        BmobFile bmobFile = new BmobFile(file);
        user.setIcon(bmobFile);
        String brithday = tvBirthday.getText().toString();
        user.setBrithday(brithday);
        String home = tvHome.getText().toString();
        user.setHome(home);
        int sex = rbMale.isChecked()?0:1;
        user.setSex(sex);
    }

    //修改下一步按钮的状态
    /**
     * 其实并不好，比如人家没有头像的时候，应该先过
     */
    private void changeNextButtonState(){
        //头像
        Drawable drawable = ivIcon.getDrawable();
        if(drawable == null){
            btNext.setEnabled(false);
            return;
        }
        //生日
        String birthday = tvBirthday.getText().toString();
        if(TextUtils.isEmpty(birthday)){
            btNext.setEnabled(false);
            return;
        }
        //家乡
        String home = tvHome.getText().toString();
        if(TextUtils.isEmpty(home)){
            btNext.setEnabled(false);
            return;
        }
        //性别
        if(!(rbMale.isChecked() || rbFemale.isChecked())){
            btNext.setEnabled(false);
            return;
        }
        btNext.setEnabled(true);
    }

    //修改性别的选择
    private void changeSexCheckState(boolean isCheckedMale) {
        rbMale.setChecked(isCheckedMale);
        rbFemale.setChecked(!isCheckedMale);
        changeNextButtonState();
    }

    //弹出选择日期的对话框
    private void showSelectDateDialog() {
        if(datePickerDialog == null){
            //日历对象
            Calendar calendar = Calendar.getInstance();
            //日期选择对话框
            datePickerDialog = DatePickerDialog.newInstance(new MyOnDateSetListener(),//日期选择监听
                    calendar.get(Calendar.YEAR),//年
                    calendar.get(Calendar.MONTH),//月
                    calendar.get(Calendar.DAY_OF_MONTH),//日
                    false);//是否震动
            datePickerDialog.setYearRange(1985, 2028);//设置年的范围
        }
        //显示
        datePickerDialog.show(getSupportFragmentManager(), "DATEPICKER_TAG");
    }

    private class MyOnDateSetListener implements DatePickerDialog.OnDateSetListener{

        @Override
        public void onDateSet(DatePickerDialog datePickerDialog, int year, int month, int day) {
            //把选择的日期显示在TextView
            tvBirthday.setText(year+"-"+month+"-"+day);
            changeNextButtonState();
        }
    }

    private CityPicker cityPicker;
    private void showSelectHomeDialog() {
        if(cityPicker == null){
            cityPicker = new CityPicker.Builder(this)//城市选择器
                    .title("选择家乡")//设置标题
                    .textSize(20)//滚轮文字的大小
                    .titleBackgroundColor("#b9b7b8")//设置标题文字的颜色
                    .onlyShowProvinceAndCity(true)//只显示省和城市
                    .cancelTextColor("#FF4081")//取消文本的颜色
                    .confirTextColor("#FF4081")//确定文本的颜色
                    .province("湖南省")//设置缺省的省
                    .city("常德市")//设置缺省的市
                    .district("无")
                    .textColor(Color.parseColor("#000000"))//滚轮文字的颜色
                    .provinceCyclic(true)//省份滚轮是否循环显示
                    .cityCyclic(false)//城市滚轮是否循环显示
                    .districtCyclic(false)//地区（县）滚轮是否循环显示
                    .visibleItemsCount(7)//滚轮显示的item个数
                    .itemPadding(10)//滚轮item间距
                    .build();
            //确定选择监听
            cityPicker.setOnCityItemClickListener(new MyOnCityItemClickListener());
        }
        cityPicker.show();
    }

    private class MyOnCityItemClickListener implements CityPicker.OnCityItemClickListener{

        @Override
        public void onSelected(String... citySelected) {
            String city = citySelected[0]+"-" + citySelected[1];
            tvHome.setText(city);
            changeNextButtonState();
        }
    }


    //从系统的相册中获取一张图片
    private void pickPictureFromSystemGallery() {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, 100);
    }

    /*
 * 剪切图片
 */
    private void crop(Uri uri) {
        // 裁剪图片意图
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        intent.putExtra("crop", "true");
        // 裁剪框的比例，1：1
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        // 裁剪后输出图片的尺寸大小
        intent.putExtra("outputX", 250);
        intent.putExtra("outputY", 250);

        intent.putExtra("outputFormat", "JPEG");// 图片格式
        intent.putExtra("noFaceDetection", true);// 取消人脸识别
        intent.putExtra("return-data", true);
        // 开启一个带有返回值的Activity，请求码为PHOTO_REQUEST_CUT
        startActivityForResult(intent, 200);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case 100://从系统相册返回的
                if (data != null) {
                    Uri uri = data.getData();
                    if (uri != null) {
                        //调用系统的图片裁剪
                        crop(uri);
                    }
                }
                break;
            case 200:
                Bitmap bitmap = data.getParcelableExtra("data");
                if (bitmap != null) {
                    //显示图片
                    ivIcon.setImageBitmap(bitmap);
                    changeNextButtonState();
                    try {
                        //保存图片（/data/packagename/files）
                        FileOutputStream stream = openFileOutput(user.getNickname() + ".jpg", Context.MODE_PRIVATE);
                        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);//把bitmap写入到流中
                    } catch (Exception e) {

                    }
                }
                break;
        }
    }


}
