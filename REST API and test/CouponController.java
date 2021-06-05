package com.example.demo;

import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@RestController
public class CouponController {

    private static List<MyCoupon> m_coupons = new ArrayList<>();
   // private static List<MyCoupon> coupons = new ArrayList<>();

    // static ctor
    static {
        m_coupons.add( new MyCoupon(1, "Caffe") );
        m_coupons.add( new MyCoupon(2, "Movie VIP") );
        m_coupons.add( new MyCoupon(3, "Sky jump") );
    }

    @GetMapping("/coupon")
    public List<MyCoupon> getCoupons()
    {
        return m_coupons;
    }

    @GetMapping("/coupon/{id}")
    public MyCoupon doGetCouponById(@PathVariable("id") int id)
    {
        for(MyCoupon c : m_coupons)
        {
            if (c.getId() == id)
            {
                return c;
            }
        }
        return null;
    }

    @PostMapping("/coupon")
    public String addCoupon(@RequestBody MyCoupon c)
    {
        m_coupons.add(c);
        return "Coupon add";
    }

    @PutMapping("/coupon/{id}")
    public String doUpdateCouponByID(@PathVariable("id") int id,
                                     @RequestBody Coupon sent)
    {
        for (MyCoupon c : m_coupons) {
            if (c.getId() == id){
                c.setId(sent.getId());
                c.setTitle(sent.getTitle());
                return  "Coupon updated";
            }
        }
        return "Coupon not found";
    }

    @DeleteMapping ("/coupon/{id}")
    public String deleteCouponById(@PathVariable("id") int id)
    {
        Iterator<MyCoupon> it = m_coupons.iterator();
        while(it.hasNext()) {
            MyCoupon i = it.next();
            if(i.getId() == id) {
                it.remove();
                return "Coupon deleted";
            }
        }
        return "Coupon not found";
    }
}