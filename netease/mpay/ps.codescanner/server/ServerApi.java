package com.netease.mpay.ps.codescanner.server;

import android.content.Context;
import com.netease.mc.mi.R;
import com.netease.mpay.ps.codescanner.net.FetchUrl;
import com.netease.mpay.ps.codescanner.server.api.Request;
import com.netease.mpay.ps.codescanner.utils.NetworkUtils;
import java.util.HashMap;

/* loaded from: classes5.dex */
public class ServerApi {
    private Context mContext;
    private HashMap<Integer, Integer> mFetchUrlErrorReason;
    private String mGameId;

    public ServerApi(Context context, String str) {
        this.mContext = context;
        this.mGameId = str;
        initErrorReason(context);
    }

    public void fetch(Request request) throws ApiCallException {
        try {
            request.parseResp(this.mContext, FetchUrl.fetchUrl(request.getMethod(), request.getURL(), request.createHeaders(this.mContext), request.createDatas(this.mContext, this.mGameId), 10000, 10000));
        } catch (FetchUrl.FetchUrlException e) {
            throw new ApiCallException(this.mContext.getString(getFetchUrlErrorReason(e.getCode()).intValue()));
        }
    }

    private void initErrorReason(Context context) {
        this.mFetchUrlErrorReason = new HashMap<>();
        this.mFetchUrlErrorReason.put(4, Integer.valueOf(R.string.netease_mpay_ps_codescanner__network_err_client_protocol));
        this.mFetchUrlErrorReason.put(5, Integer.valueOf(R.string.netease_mpay_ps_codescanner__network_err_request_method));
        this.mFetchUrlErrorReason.put(2, Integer.valueOf(R.string.netease_mpay_ps_codescanner__network_err_server_status));
        this.mFetchUrlErrorReason.put(3, Integer.valueOf(R.string.netease_mpay_ps_codescanner__network_err_server_read));
        this.mFetchUrlErrorReason.put(1, Integer.valueOf(R.string.netease_mpay_ps_codescanner__network_err_param_encoding));
        this.mFetchUrlErrorReason.put(6, Integer.valueOf(R.string.netease_mpay_ps_codescanner__network_err_no_perr_certificate));
        this.mFetchUrlErrorReason.put(8, Integer.valueOf(R.string.netease_mpay_ps_codescanner__network_err_no_perr_certificate_date_error));
        this.mFetchUrlErrorReason.put(7, Integer.valueOf(R.string.netease_mpay_ps_codescanner__network_err_invalid_url_address));
        this.mFetchUrlErrorReason.put(9, Integer.valueOf(R.string.netease_mpay_ps_codescanner__network_err_illegal_param));
    }

    private Integer getFetchUrlErrorReason(int i) {
        Integer num = this.mFetchUrlErrorReason.get(Integer.valueOf(i));
        if (num == null) {
            return Integer.valueOf(R.string.netease_mpay_ps_codescanner__network_err_fetchurl_others);
        }
        return ((i == 4 || i == 3) && NetworkUtils.isCmwapNet(this.mContext)) ? Integer.valueOf(R.string.netease_mpay_ps_codescanner__network_err_cmwap_bad_network) : num;
    }
}