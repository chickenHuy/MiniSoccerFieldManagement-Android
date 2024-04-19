package vn.id.nguyenthanhhuy.minisoccerfieldmanagement.service;

import android.content.Context;

import java.math.BigDecimal;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

import vn.id.nguyenthanhhuy.minisoccerfieldmanagement.DAO.IMembershipDAO;
import vn.id.nguyenthanhhuy.minisoccerfieldmanagement.DAO.MembershipDAOImpl;
import vn.id.nguyenthanhhuy.minisoccerfieldmanagement.model.Membership;

public class MembershipServiceImpl implements IMembershipService {
    IMembershipDAO membershipDAO;
    public MembershipServiceImpl(Context context) {
        this.membershipDAO = new MembershipDAOImpl(context);
    }
    @Override
    public Boolean add(Membership membership) {
        return membershipDAO.add(membership);
    }

    @Override
    public Boolean update(Membership membership) {
        return membershipDAO.update(membership);
    }

    @Override
    public Boolean softDelete(String id) {
        return membershipDAO.softDelete(id);
    }

    @Override
    public Membership findById(String id) {
        return membershipDAO.findById(id);
    }

    @Override
    public List<Membership> findAll() {
        return membershipDAO.findAll();
    }

    @Override
    public Membership findBySpendAmount(BigDecimal totalSpend) {
        List<Membership> memberships = membershipDAO.findAll();
        Optional<Membership> result = memberships.stream()
                .filter(membership -> totalSpend.compareTo(membership.getMinimumSpendAmount()) >= 0)
                .max(Comparator.comparing(Membership::getMinimumSpendAmount));

        return result.orElse(null);
    }
}
